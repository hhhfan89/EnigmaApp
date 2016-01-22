package it.divito.enigma;

import it.divito.enigma.database.DatabaseAdapter;
import it.divito.enigma.util.Constants;
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
		
		/*
		WifiManager wifiManager = (WifiManager) this.getSystemService(Context.WIFI_SERVICE);
		Log.d("wifiManager", ""+wifiManager.isWifiEnabled());
		if(wifiManager.isWifiEnabled()) {
		    // WIFI ALREADY ENABLED. GRAB THE MAC ADDRESS HERE
		    WifiInfo info = wifiManager.getConnectionInfo();
		    String macAddress = info.getMacAddress();
		    Log.d("macAddress", macAddress);
		} else {
		    // ENABLE THE WIFI FIRST
		    wifiManager.setWifiEnabled(true);

		    // WIFI IS NOW ENABLED. GRAB THE MAC ADDRESS HERE
		    WifiInfo info = wifiManager.getConnectionInfo();
		    String macAddress = info.getMacAddress();
		    Log.d("macAddress", macAddress);
		  //  wifiManager.setWifiEnabled(false);
		}
		
		*/
		
		
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
		if(checkDevice()) {
			startGame();
		}
	}
	
	private boolean checkDevice() {
		dbAdapter.open(); 
		imeiNumber = getImeiNumber();
		deviceName = getDeviceName();
		macAddress = getMacAddress();
		
		long livesLeft = dbAdapter.insert(imeiNumber, deviceName, macAddress);
		dbAdapter.close();
		
		// Nessuna vita rimasta (su DB locale)
		if(livesLeft == 0) {
			createNoLivesAlertDialog().show();
			return false;
		}
		
		new RetrieveFeedTask().execute();
		return true;
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
	
	class RetrieveFeedTask extends AsyncTask<Void, Long, Boolean> {

		int livesLeft;
		
		
	    protected Boolean doInBackground(Void... params) {
	        try {
	        	Client client = new Client("http://10.0.3.2:8080/AlessioWebapp/users/");
				ClientResponse clientResponse = client.postBaseURI(imeiNumber, macAddress, deviceName, "checkUser");
				livesLeft = clientResponse.getLivesLeft();
				dbAdapter.updateUser(1, clientResponse.getIdOnRemoteDB());
	        } catch (Exception e) {
	        	return false;
	        }
	        return true;
	    }
	    
	    public int getLivesLeft() {
	    	return livesLeft;
	    }

	    @Override
	    protected void onPostExecute(Boolean result) {
	    	Intent intent = new Intent(MainActivity.this, QuestionActivity.class);
			intent.putExtra("imeiNumber", imeiNumber);
			intent.putExtra("deviceName", deviceName);
			intent.putExtra("macAddress", macAddress);
			startActivity(intent);
	    }
	    
	    @Override
	    protected void onProgressUpdate(Long... progress) {
	        //int percent = (int)(100.0*(double)progress[0]/mFileLen + 0.5);
	        //mDialog.setProgress(percent);
	    	ProgressDialog.show(MainActivity.this, "Loading", "Wait while loading...");
	    }

	}
}


