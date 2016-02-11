package it.divito.enigma.database;

import it.divito.enigma.MyApplication;
import it.divito.enigma.util.Constants;

import java.util.Date;

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

public class QuestionsTable {
	
	private SQLiteDatabase database;
	private MyApplication myApp;
	
	public QuestionsTable(Context context) {
		myApp = (MyApplication) context.getApplicationContext();
		database = myApp.getDatabase();
	}
	
	
	public long insertQuestion(String question, int level, int answerTime) {
		ContentValues values = new ContentValues();
		values.put(Constants.COLUMN_QUESTION, question);
		values.put(Constants.COLUMN_LEVEL, level);
		values.put(Constants.COLUMN_ANSWER_TIME, answerTime);
//		values.put(Constants.COLUMN_START_TIME, "CURRENT_TIMESTAMP");
		long result;
		try {
			result = database.insertOrThrow(Constants.TABLE_QUESTIONS, null, values);
		} catch (SQLiteConstraintException e) {
			result = -1;
		}
		return result;
	}
	
	/*
	public boolean insertStartTime(Date startTime) {
		// TODO: check!
		ContentValues value = new ContentValues();
		value.put(Constants.COLUMN_START_TIME, "CURRENT_TIMESTAMP");
		return database.update(Constants.TABLE_USERS, value, Constants.COLUMN_ID, new String[]{"1"}) > 0;
	}
	*/
	public boolean insertAnswer(String answer) {
		ContentValues value = new ContentValues();
		value.put(Constants.COLUMN_ANSWER, answer);
		return database.update(Constants.TABLE_USERS, value, Constants.COLUMN_ID, new String[]{"1"}) > 0;
	}
	
/*	
	create.append("	  "+ Constants.COLUMN_ID   			+" INTEGER PRIMARY KEY,");// AUTOINCREMENT,");
	create.append("   "+ Constants.COLUMN_QUESTION 		+" TEXT NOT NULL,");
	create.append("   "+ Constants.COLUMN_ANSWER		+" TEXT NOT NULL,");
	create.append("   "+ Constants.COLUMN_LEVEL			+" INTEGER,");
	create.append("   "+ Constants.COLUMN_START_TIME 	+" DATETIME DEFAULT CURRENT_TIMESTAMP, ");
	create.append("   "+ Constants.COLUMN_ANSWER_TIME	+" INTEGER, ");
	create.append("   "+ Constants.COLUMN_ID_ON_REMOTE_DB + " INTEGER, ");
	create.append(")");*/
	
	public Question selectQuestion() {
		Cursor c = database.query(Constants.TABLE_QUESTIONS, null, null, null, null, null, null);
		Question question = new Question();
		if(c.moveToNext()) {
			question.setAnswerTime(c.getInt(c.getColumnIndex(Constants.COLUMN_ANSWER_TIME)));
			question.setQuestion(c.getString(c.getColumnIndex(Constants.COLUMN_QUESTION)));
			question.setLevel(c.getInt(c.getColumnIndex(Constants.COLUMN_LEVEL)));
			question.setIdOnRemoteDB(c.getInt(c.getColumnIndex(Constants.COLUMN_ID_ON_REMOTE_DB)));
			question.setStartTime(new Date(c.getLong(c.getColumnIndex(Constants.COLUMN_START_TIME))));		// TODO CHECK!
		}
		return question;
	}
	
}