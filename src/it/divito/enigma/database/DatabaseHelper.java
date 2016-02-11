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
		db.execSQL(createUsersTable());
		db.execSQL(createQuestionsTable());
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// non previsto
	}
	
	private String createUsersTable(){
		StringBuffer create = new StringBuffer();
		create.append("CREATE TABLE " + Constants.TABLE_USERS + "(");
		create.append("	  "+ Constants.COLUMN_ID   			+" INTEGER,");// AUTOINCREMENT,";
		create.append("   "+ Constants.COLUMN_IMEI 			+" TEXT NOT NULL,");
		create.append("   "+ Constants.COLUMN_DEVICE_NAME	+" TEXT NOT NULL,");
		create.append("   "+ Constants.COLUMN_MAC  			+" TEXT NOT NULL,");
		create.append("   "+ Constants.COLUMN_LIVES			+" INTEGER, ");
		create.append("   "+ Constants.COLUMN_ID_ON_REMOTE_DB + " INTEGER, ");
		create.append("   PRIMARY KEY(" + Constants.COLUMN_IMEI + ", " + Constants.COLUMN_DEVICE_NAME + ", " + Constants.COLUMN_MAC + ")");
		create.append(")");
		return create.toString();
	}
	
	private String createQuestionsTable() {
		StringBuffer create = new StringBuffer();
		create.append("CREATE TABLE " + Constants.TABLE_QUESTIONS + "(");
		create.append("	  "+ Constants.COLUMN_ID   			+" INTEGER PRIMARY KEY,");// AUTOINCREMENT,");
		create.append("   "+ Constants.COLUMN_QUESTION 		+" TEXT NOT NULL,");
		create.append("   "+ Constants.COLUMN_ANSWER		+" TEXT NOT NULL,");
		create.append("   "+ Constants.COLUMN_LEVEL			+" INTEGER,");
		create.append("   "+ Constants.COLUMN_START_TIME 	+" DATETIME DEFAULT CURRENT_TIMESTAMP, ");
		create.append("   "+ Constants.COLUMN_ANSWER_TIME	+" INTEGER, ");
		create.append("   "+ Constants.COLUMN_ID_ON_REMOTE_DB + " INTEGER ");
		create.append(")");
		return create.toString();
	}
	
}
