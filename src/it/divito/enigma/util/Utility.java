package it.divito.enigma.util;

import it.divito.enigma.database.Question;
import it.divito.enigma.ws.ClientResponse;
import it.divito.enigma.ws.QuestionResponse;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.widget.Toast;

public class Utility {

	public static void showToast(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
    }
	
	public static ClientResponse parseUserResponse(JSONObject jsonResponse) throws JSONException {
		ClientResponse clientResponse = new ClientResponse();
		if(jsonResponse!=null) {
			clientResponse.setAlreadyRegistered(Boolean.parseBoolean((String) jsonResponse.get("alreadyRegistered")));
			clientResponse.setLivesLeft(Integer.parseInt((String) jsonResponse.get("livesLeft")));
			clientResponse.setIdOnRemoteDB(Integer.parseInt((String) jsonResponse.get("idOnRemoteDB")));
			clientResponse.setLevel(Integer.parseInt((String) jsonResponse.get("level")));
		}
        return clientResponse;
	}
	
	public static ClientResponse createClientResponseError(String errorMessage) {
		ClientResponse clientResponse = new ClientResponse();
		clientResponse.setIdOnRemoteDB(-1);
		clientResponse.setLivesLeft(-1);
		clientResponse.setErrorMessage(errorMessage);
		return clientResponse;
	}
	
	public static QuestionResponse parseQuestionResponse(JSONObject jsonResponse) throws JSONException {
		QuestionResponse questionResponse = new QuestionResponse();
		if(jsonResponse!=null) {
			questionResponse.setQuestion((String) jsonResponse.get("question"));
			questionResponse.setLevel(Integer.parseInt((String) jsonResponse.get("level")));
			questionResponse.setIdOnRemoteDb(Integer.parseInt((String) jsonResponse.get("idOnRemoteDB")));
			questionResponse.setAnswerTime(Integer.parseInt((String) jsonResponse.get("answerTime")));
		}
        return questionResponse;
	}
	
	public static Question mappingQuestionResponseToQuestion(QuestionResponse questionResponse){
		Question question = new Question();
		if(questionResponse!=null) {
			question.setAnswerTime(questionResponse.getAnswerTime());
			question.setIdOnRemoteDB(questionResponse.getIdOnRemoteDb());
			question.setLevel(questionResponse.getLevel());
			question.setQuestion(questionResponse.getQuestion());
			question.setStartTime(questionResponse.getStartTime());
		}
		return question;
	}
}
