package it.divito.enigma.database;

import java.util.Date;

public class Question {
	
	private long idOnRemoteDB;
	private String question;
	private long answerTime;
	private Date startTime;
	private int level;
	
	public Question() {}

	public long getIdOnRemoteDB() {
		return idOnRemoteDB;
	}

	public void setIdOnRemoteDB(long idOnRemoteDB) {
		this.idOnRemoteDB = idOnRemoteDB;
	}

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	public long getAnswerTime() {
		return answerTime;
	}

	public void setAnswerTime(long answerTime) {
		this.answerTime = answerTime;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	
	
	
}