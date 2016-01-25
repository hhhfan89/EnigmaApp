package it.divito.enigma;

import it.divito.enigma.database.DatabaseAdapter;
import it.divito.enigma.util.Constants;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

public class QuestionActivity extends Activity {
	
	private MyApplication myApp;
	private DatabaseAdapter dbAdapter;
	private String imeiNumber;
	private String deviceName;
	private String macAddress;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.question);

		myApp = ((MyApplication) this.getApplication());
		dbAdapter = myApp.getDbAdapter();
	}
	
	public void sendAnswer(View v) {
		getPhoneInfo();
		int livesLeft = checkLives();
		if(livesLeft > 0) {
			removeLife(livesLeft);
			String answer = ((EditText) findViewById(R.id.answer)).getText().toString();
			if(checkAnswer(answer)) {
				
			} else {
				Toast.makeText(this, "Wrong answer!", Toast.LENGTH_LONG).show();
			}
		} else {
			Toast.makeText(this, "No lives!", Toast.LENGTH_LONG).show();
		}
	}
	

	public void getPhoneInfo() {
		Intent intent = getIntent();
		imeiNumber = intent.getStringExtra(Constants.IMEI_NUMBER);
		deviceName = intent.getStringExtra(Constants.DEVICE_NAME);
		macAddress = intent.getStringExtra(Constants.MAC_ADDRESS);
	}
	
	
	private int checkLives() {
		dbAdapter.open();
		int lives = dbAdapter.checkLives(imeiNumber, deviceName, macAddress).getLivesLeft();
		dbAdapter.close();	
		return lives;
	}
	
	
	private boolean removeLife(int livesLeft) {
		dbAdapter.open();
		boolean lifeRemoved = dbAdapter.removeLife(imeiNumber, deviceName, macAddress, livesLeft);
		dbAdapter.close();	
		return lifeRemoved;
	}
	
	
	public boolean checkAnswer(String answer) {
		return false;
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
	
}