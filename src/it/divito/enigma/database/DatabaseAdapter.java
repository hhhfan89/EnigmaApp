package it.divito.enigma.database;

import it.divito.enigma.MyApplication;
import it.divito.enigma.util.Constants;
import android.content.ContentValues;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
 
public class DatabaseAdapter {
	
    private Context context;
    private SQLiteDatabase database;
    private DatabaseHelper dbHelper;
    private MyApplication myApp;
    private UsersTable usersTable;
    private QuestionsTable questionsTable;
   
    public DatabaseAdapter(Context context) {
    	this.context = context;
    }
 
   
    public DatabaseAdapter open() throws SQLException {
    	
    	dbHelper = new DatabaseHelper(context);
    	database = dbHelper.getWritableDatabase();

    	myApp = (MyApplication) context.getApplicationContext();
		myApp.setDatabase(database);
    	
		usersTable = new UsersTable(context);
		questionsTable = new QuestionsTable(context);
		
    	return this;
    }
 
   
    public void close() {
    	dbHelper.close();
    }
 
	// USER - START
	public UserInfo insert(String imeiNumber, String deviceName, String macAddress) {
		return usersTable.insert(imeiNumber, deviceName, macAddress);
	}
	
	public UserInfo checkLives(String imeiNumber, String deviceName, String macAddress) {
		return usersTable.checkLives(imeiNumber, deviceName, macAddress);
	}

	public boolean removeLife(String imeiNumber, String deviceName, String macAddress, int livesLeft) {
		return usersTable.removeLife(imeiNumber, deviceName, macAddress, livesLeft);
	}
	
	public boolean addLife(String imeiNumber, String deviceName, String macAddress) {
		return usersTable.addLife(imeiNumber, deviceName, macAddress);
	}

	public boolean updateUser(int idOnLocalDB, int idOnRemoteDB) {
		return usersTable.updateUser(idOnLocalDB, idOnRemoteDB);
	}
	// USER - END
	
	
	
	// QUESTION - START
	public boolean insertQuestion(String question, int level, int answerTime) {
		return questionsTable.insertQuestion(question, level, answerTime) > 0 ? true : false;
	}
	
	public boolean insertAnswer(String answer) {
		return questionsTable.insertAnswer(answer);
	}
	
	public Question selectQuestion() {
		return questionsTable.selectQuestion();
	}
	// QUESTION - END
}