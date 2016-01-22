package it.divito.enigma;

import it.divito.enigma.database.DatabaseAdapter;
import android.app.Application;
import android.database.sqlite.SQLiteDatabase;


/**
 * This class maintains global application state. 
 * 
 * @author Stefano
 *
 */
public class MyApplication extends Application {
	
	public DatabaseAdapter dbAdapter = new DatabaseAdapter(this);
	public SQLiteDatabase database;
	public int numeroPartite = 0;

	public DatabaseAdapter getDbAdapter() {
		return dbAdapter;
	}

	public void setDbAdapter(DatabaseAdapter dbAdapter) {
		this.dbAdapter = dbAdapter;
	}

	public SQLiteDatabase getDatabase() {
		return database;
	}

	public void setDatabase(SQLiteDatabase database) {
		this.database = database;
	}
	
	public int getNumeroPartite(){
		return numeroPartite;
	}

	public void setNumeroPartite(int numeroPartite) {
		this.numeroPartite = numeroPartite;
	}
	
	
	
}