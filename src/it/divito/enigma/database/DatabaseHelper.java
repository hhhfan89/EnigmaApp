package it.divito.enigma.database;

import it.divito.enigma.util.Constants;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

	public DatabaseHelper(Context context) {
		super(context, Constants.DATABASE_NAME, null, Constants.DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("PRAGMA foreign_keys=ON;");
		db.execSQL(createTable());
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// non previsto
	}
	
	private String createTable(){
		String create = "";
		create += "CREATE TABLE " + Constants.TABLE_NAME + "(";
		create += "	  "+ Constants.COLUMN_ID   			+" INTEGER,";// AUTOINCREMENT,";
		create += "   "+ Constants.COLUMN_IMEI 			+" TEXT NOT NULL,";
		create += "   "+ Constants.COLUMN_DEVICE_NAME	+" TEXT NOT NULL,";
		create += "   "+ Constants.COLUMN_MAC  			+" TEXT NOT NULL,";
		create += "   "+ Constants.COLUMN_LIVES			+" INTEGER, ";
		create += "   "+ Constants.COLUMN_ID_ON_REMOTE_DB + " INTEGER, ";
		create += "   PRIMARY KEY(" + Constants.COLUMN_IMEI + ", " + Constants.COLUMN_DEVICE_NAME + ", " + Constants.COLUMN_MAC + ")";
		create += ")";
		return create;
	}
	
}
