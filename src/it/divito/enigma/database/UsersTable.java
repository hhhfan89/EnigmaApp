package it.divito.enigma.database;

import it.divito.enigma.MyApplication;
import it.divito.enigma.util.Constants;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
 

/**
 * User table
 * @author Stefano Di Vito
 *
 */

public class UsersTable {
	
	private SQLiteDatabase database;
	private MyApplication myApp;
	
	public UsersTable(Context context) {
		myApp = (MyApplication) context.getApplicationContext();
		database = myApp.getDatabase();
	}
	
	
	private ContentValues createContentValues(String imeiNumber, String deviceName, String macAddress) {
		ContentValues values = new ContentValues();
		values.put(Constants.COLUMN_IMEI, imeiNumber);
		values.put(Constants.COLUMN_DEVICE_NAME, deviceName);
		values.put(Constants.COLUMN_MAC, macAddress);
		values.put(Constants.COLUMN_LIVES, 1);
		return values;
	}
	
	private ContentValues createUpdateValues(int life) {
		ContentValues values = new ContentValues();
		values.put(Constants.COLUMN_LIVES, life);
		return values;
	}
	
	public long insert(String imeiNumber, String deviceName, String macAddress) {
		ContentValues initialValues = createContentValues(imeiNumber, deviceName, macAddress);
		long result;
		try {
			result = database.insertOrThrow(Constants.TABLE_NAME, null, initialValues);
		} catch (SQLiteConstraintException e) {
			// Vuol dire che è già presente nel DB, ma comunque può avere altre vite..
			result = checkLives(imeiNumber, deviceName, macAddress);
		}
		return result;
	}
	
	
	public int checkLives(String imeiNumber, String deviceName, String macAddress) {
		String selection = Constants.COLUMN_IMEI + " = ? AND "
						 + Constants.COLUMN_DEVICE_NAME + " = ? AND " 
						 + Constants.COLUMN_MAC + " = ?";
		Cursor c = database.query(Constants.TABLE_NAME, new String[]{Constants.COLUMN_LIVES}, selection, new String[]{imeiNumber, deviceName, macAddress}, null, null, null);
		if(c.moveToNext())
			return c.getInt(0);
		return -1;
	}


	public boolean removeLife(String imeiNumber, String deviceName, String macAddress, int life) {
		ContentValues updateValues = createUpdateValues(--life);
		String whereClause = Constants.COLUMN_IMEI + " = ? AND "
						   + Constants.COLUMN_DEVICE_NAME + " = ? AND " 
						   + Constants.COLUMN_MAC + " = ?";
		String[] whereArgs = new String[]{imeiNumber, deviceName, macAddress};
		return database.update(Constants.TABLE_NAME, updateValues, whereClause, whereArgs) > 0;
	}


	public boolean addLife(String imeiNumber, String deviceName,String macAddress) {
		ContentValues updateValues = createUpdateValues(1);
		String whereClause = Constants.COLUMN_IMEI + " = ? AND "
						   + Constants.COLUMN_DEVICE_NAME + " = ? AND " 
						   + Constants.COLUMN_MAC + " = ?";
		String[] whereArgs = new String[]{imeiNumber, deviceName, macAddress};
		return database.update(Constants.TABLE_NAME, updateValues, whereClause, whereArgs) > 0;
	}


	public boolean updateUser(int idOnLocalDB, int idOnRemoteDB) {
		ContentValues updateValues = new ContentValues();
		updateValues.put(Constants.COLUMN_ID_ON_REMOTE_DB, idOnRemoteDB);
		String whereClause = Constants.COLUMN_ID + " = ?";
		String[] whereArgs = new String[]{Integer.toString(idOnLocalDB)};
		return database.update(Constants.TABLE_NAME, updateValues, whereClause, whereArgs) > 0;
	}
	
}