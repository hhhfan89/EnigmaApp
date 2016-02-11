package it.divito.enigma.ws;

import it.divito.enigma.database.DatabaseAdapter;
import it.divito.enigma.database.Question;
import it.divito.enigma.util.Constants;
import it.divito.enigma.util.Utility;

import java.io.File;
import java.util.Date;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

public class RetrieveQuestionTask extends AsyncTask<Void, Long, QuestionResponse> {

	private ProgressDialog mDialog;
	private Context mContext;
	private int level;
	private DatabaseAdapter dbAdapter;
	private Question question;

	public RetrieveQuestionTask(Context context, DatabaseAdapter dbAdapter, int level) {
		this.mContext = context;
		this.level = level;
		this.dbAdapter = dbAdapter;
		mDialog = new ProgressDialog(context);
		mDialog.setMessage("Recupero enigma in corso..");
		mDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		mDialog.show();
	}

	protected QuestionResponse doInBackground(Void... params) {
		QuestionResponse questionResponse = new QuestionResponse();
		try {
			Client client = new Client(Constants.WS_HOST + File.separator + Constants.WS_APP_NAME);
			int tentativi = 0;
			while(questionResponse.getIdOnRemoteDb()<=0 && tentativi<5) {
				questionResponse = client.getQuestion(Constants.WS_OPERATION_GET_QUESTION, level);
				tentativi++;
			}

			if(questionResponse.getIdOnRemoteDb()>0 && questionResponse.getQuestion()!=null) {
				dbAdapter.insertQuestion(questionResponse.getQuestion(), questionResponse.getLevel(), questionResponse.getAnswerTime());
				questionResponse.setStartTime(new Date());
			} 
			
		} catch (Exception e) {
		}
		
		return questionResponse;
	}

	@Override
	protected void onPostExecute(QuestionResponse result) {
		mDialog.dismiss();
		setQuestion(Utility.mappingQuestionResponseToQuestion(result));
	}

	@Override
	protected void onProgressUpdate(Long... progress) {
		int percent = (int)(100.0*(double)progress[0] + 0.5);
		mDialog.setProgress(percent);
	}

	public Question getQuestion() {
		return question;
	}

	public void setQuestion(Question question) {
		this.question = question;
	}
	
}
