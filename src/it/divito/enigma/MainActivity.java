package it.divito.enigma;

import java.io.File;

import it.divito.enigma.database.DatabaseAdapter;
import it.divito.enigma.database.UserInfo;
import it.divito.enigma.util.Constants;
import it.divito.enigma.util.Utility;
import it.divito.enigma.ws.Client;
import it.divito.enigma.ws.ClientResponse;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

public class MainActivity extends Activity {
	
	private MyApplication myApp;
	private DatabaseAdapter dbAdapter;
	private String imeiNumber;
	private String deviceName;
	private String macAddress;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		myApp = ((MyApplication) this.getApplication());
		dbAdapter = myApp.getDbAdapter();
		
		// TODO: tenersi in memoria l'idRemoto finché l'app è aperta, così evito di accedere n volte al db locale
		// (tanto è una variabile int, niente di che)
	}
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onResume() {
		super.onResume();
	}
	
	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main, container,
					false);
			return rootView;
		}
	}

	public String getImeiNumber() {
		TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		return telephonyManager.getDeviceId();
	}
	
	
	public String getMacAddress() {
		// Si può ricavare il macAddress solo dalla versione 6 in giù, altrimenti restituisce sempre una costante
		int currentApiVersion = android.os.Build.VERSION.SDK_INT;
		if(currentApiVersion < 23) {
			WifiManager wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
			return wifiManager.getConnectionInfo().getMacAddress();
		}
		return Constants.NOT_AVAILABLE;
	}
	
	
	public String getDeviceName() {
	    String manufacturer = Build.MANUFACTURER;
	    String model = Build.MODEL;
	    if (model.startsWith(manufacturer)) {
	        return capitalize(model);
	    } else {
	        return capitalize(manufacturer) + " " + model;
	    }
	}


	private String capitalize(String s) {
	    if (s == null || s.length() == 0) {
	        return "";
	    }
	    char first = s.charAt(0);
	    if (Character.isUpperCase(first)) {
	        return s;
	    } else {
	        return Character.toUpperCase(first) + s.substring(1);
	    }
	} 
	
	public void play(View v) {
//		if(checkDevice()) {
//			startGame();
//		}
		checkDevice();
	}
	
	private void checkDevice() {
		dbAdapter.open(); 
		imeiNumber = getImeiNumber();
		deviceName = getDeviceName();
		macAddress = getMacAddress();
		
		// Ritorna userInfo(livesLeft, idOnRemoteDB):
		//		- livesLeft: 1 se appena inserito, n se inserito in precedenza
		//		- idOnRemoteDB: 0 se appena inserito, n se inserito in precedenza
		UserInfo userInfo = dbAdapter.insert(imeiNumber, deviceName, macAddress);
		userInfo.setImei(imeiNumber);
		userInfo.setDeviceName(deviceName);
		userInfo.setMacAddress(macAddress);
		dbAdapter.close();
		
		// Nessuna vita rimasta (su DB locale)
		if(userInfo.getLivesLeft() == 0) {
			createNoLivesAlertDialog().show();
		}
		
		new RetrieveFeedTask(this, userInfo.getIdOnRemoteDB()!=0 ? true : false, userInfo).execute();
	}
	
	private AlertDialog createNoLivesAlertDialog() {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
		alertDialogBuilder.setCancelable(false).setMessage("Non hai altre vite per giocare, ne vuoi comprare una?");

		// Imposta il messaggio
		alertDialogBuilder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
            public void onClick(final DialogInterface dialog, final int id) {
            	dbAdapter.open(); 
            	dbAdapter.addLife(imeiNumber, deviceName, macAddress);
            	dbAdapter.close();
            	Toast.makeText(getApplicationContext(), "Go to the game!", Toast.LENGTH_SHORT).show();
            	startGame();
            	//startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));     
            }
        });

		alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
    		public void onClick(DialogInterface dialog,int id) {
    			dialog.cancel();
    		}
        });
		
		return alertDialogBuilder.create();
	}
	
	private void startGame() {
		Intent intent = new Intent(this, QuestionActivity.class);
		intent.putExtra("imeiNumber", imeiNumber);
		intent.putExtra("deviceName", deviceName);
		intent.putExtra("macAddress", macAddress);
		startActivity(intent);
	}
	
	class RetrieveFeedTask extends AsyncTask<Void, Long, String> {

		private int livesLeft;
		private ProgressDialog mDialog;
		private boolean canPlay;
		
		private Context mContext;
		private boolean hasIdOnRemoteDb;
		private UserInfo userInfo;
		
		public RetrieveFeedTask(Context context, boolean hasIdOnRemoteDb, UserInfo userInfo) {
			this.canPlay = false;
			this.mContext = context;
			this.userInfo = userInfo;
			this.hasIdOnRemoteDb = hasIdOnRemoteDb;
			mDialog = new ProgressDialog(context);
	        mDialog.setMessage("Connessione in corso..");
	        mDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
	        mDialog.show();
		}
		
	    protected String doInBackground(Void... params) {
	    	ClientResponse clientResponse = new ClientResponse();
	        try {
	        	Client client = new Client(Constants.WS_HOST + File.separator + Constants.WS_APP_NAME);
	        	int tentativi = 0;
	        	while(clientResponse.getIdOnRemoteDB()<=0 && tentativi<5) {
	        		clientResponse = client.postBaseURI(userInfo, hasIdOnRemoteDb ? Constants.WS_OPERATION_CHECK_USER : Constants.WS_OPERATION_SAVE_USER);
	        		tentativi++;
	        	}

	        	// Se è stato recuperato l'id remoto, faccio l'update, altrimenti.. (TODO)
				if(clientResponse.getIdOnRemoteDB()>0) {
					livesLeft = clientResponse.getLivesLeft();
					dbAdapter.updateUser(1, clientResponse.getIdOnRemoteDB());
					canPlay = true;
				} else {
					// TODO: gestione errore remoto (id su db remote non ritornato)
					canPlay = false;
				}
	        } catch (Exception e) {
	        	canPlay = false;
	        }
	        return clientResponse.getErrorMessage();
	    }
	    
	    public int getLivesLeft() {
	    	return livesLeft;
	    }

	    @Override
	    protected void onPostExecute(String result) {
	    	mDialog.dismiss();
	    	if(canPlay) {
		    	Intent intent = new Intent(MainActivity.this, QuestionActivity.class);
				intent.putExtra("imeiNumber", imeiNumber);
				intent.putExtra("deviceName", deviceName);
				intent.putExtra("macAddress", macAddress);
				startActivity(intent); 
			} else {
				AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);
				alertDialogBuilder.setCancelable(false).setMessage(result);
				alertDialogBuilder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
		    		public void onClick(DialogInterface dialog,int id) {
		    			dialog.cancel();
		    		}
		        });
				alertDialogBuilder.create().show();
			}
	    }
	    
	    @Override
	    protected void onProgressUpdate(Long... progress) {
	        int percent = (int)(100.0*(double)progress[0] + 0.5);
	        mDialog.setProgress(percent);
	    }
	    
	}
}


