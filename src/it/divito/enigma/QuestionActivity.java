package it.divito.enigma;

import it.divito.enigma.database.DatabaseAdapter;
import it.divito.enigma.database.Question;
import it.divito.enigma.util.Constants;
import it.divito.enigma.ws.QuestionResponse;
import it.divito.enigma.ws.RetrieveQuestionTask;

import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class QuestionActivity extends Activity {
	
	private MyApplication myApp;
	private DatabaseAdapter dbAdapter;
	private long idOnRemoteDB;
	private int userLevel;
	
	private Date mStartTime;
	private int mAnswerTime;
	private Date mEndTime;
	
//	private TextView mQuestionTextView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.question);

		TextView questionTextView = (TextView) findViewById(R.id.question);
		
		myApp = ((MyApplication) this.getApplication());
		dbAdapter = myApp.getDbAdapter();
		
		Question question = loadQuestion();
		if(question!=null) {
			questionTextView.setText(question.getQuestion());
		}
		
	}
	
	public void sendAnswer(View v) {
		//int livesLeft = checkLives();
		/*
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
		*/
	}


	public Question loadQuestion() {
		
		// Prima prova a caricare la domanda in locale..
		Question localQuestion = loadQuestionFromLocalDb();
		if(localQuestion!=null) {
			return localQuestion;
		}
		
		getUserInfo();
		
		RetrieveQuestionTask retrieveQuestionTask = new RetrieveQuestionTask(this, dbAdapter, userLevel);
		Question question = retrieveQuestionTask.getQuestion();
		
		Calendar c = Calendar.getInstance();
		c.setTime(question.getStartTime());
		c.add(Calendar.SECOND, (int) question.getAnswerTime());			//TODO check!
		mEndTime = c.getTime();
		
		return question;
	}

	
	private Question loadQuestionFromLocalDb() {
		return dbAdapter.selectQuestion();
	}

	public void getUserInfo() {
		Intent intent = getIntent();
		idOnRemoteDB = intent.getLongExtra(Constants.INTENT_ID_ON_REMOTE_DB, -1);
		userLevel = intent.getIntExtra(Constants.INTENT_USER_LEVEL, -1);
	}
	
	
	private boolean removeLife(int livesLeft) {
		dbAdapter.open();
//		boolean lifeRemoved = dbAdapter.removeLife(imeiNumber, deviceName, macAddress, livesLeft);
		dbAdapter.close();	
		return false;
	}
	
	
	public boolean checkAnswer(String answer) {
		return false;
	}
	
}