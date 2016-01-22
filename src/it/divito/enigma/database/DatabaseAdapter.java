package it.divito.enigma.database;

import it.divito.enigma.MyApplication;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
 
public class DatabaseAdapter {
	
    private Context context;
    private SQLiteDatabase database;
    private DatabaseHelper dbHelper;
    private MyApplication myApp;
    private UsersTable settingsTable;
   
    public DatabaseAdapter(Context context) {
    	this.context = context;
    }
 
   
    public DatabaseAdapter open() throws SQLException {
    	
    	dbHelper = new DatabaseHelper(context);
    	database = dbHelper.getWritableDatabase();

    	myApp = (MyApplication) context.getApplicationContext();
		myApp.setDatabase(database);
    	settingsTable = new UsersTable(context);

    	return this;
    
    }
 
   
    public void close() {
    	dbHelper.close();
    }
 
	
	public long insert(String imeiNumber, String deviceName, String macAddress) {
		return settingsTable.insert(imeiNumber, deviceName, macAddress);
	}


	public int checkLives(String imeiNumber, String deviceName, String macAddress) {
		return settingsTable.checkLives(imeiNumber, deviceName, macAddress);
	}


	public boolean removeLife(String imeiNumber, String deviceName, String macAddress, int livesLeft) {
		return settingsTable.removeLife(imeiNumber, deviceName, macAddress, livesLeft);
	}
	
	
	public boolean addLife(String imeiNumber, String deviceName, String macAddress) {
		return settingsTable.addLife(imeiNumber, deviceName, macAddress);
	}


	public boolean updateUser(int idOnLocalDB, int idOnRemoteDB) {
		return settingsTable.updateUser(idOnLocalDB, idOnRemoteDB);
	}
	
	
}