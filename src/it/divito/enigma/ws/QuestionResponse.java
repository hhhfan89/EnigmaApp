package it.divito.enigma.ws;

import java.util.Date;

public class QuestionResponse {
	
	private String question;
	private int level;
	private int idOnRemoteDb;
	private int answerTime;			// in seconds
	private Date startTime;
	
	public QuestionResponse() {}
	
	public int getIdOnRemoteDb() {
		return idOnRemoteDb;
	}

	public void setIdOnRemoteDb(int idOnRemoteDb) {
		this.idOnRemoteDb = idOnRemoteDb;
	}

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getAnswerTime() {
		return answerTime;
	}

	public void setAnswerTime(int answerTime) {
		this.answerTime = answerTime;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

}