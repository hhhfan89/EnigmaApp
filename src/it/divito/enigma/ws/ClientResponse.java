package it.divito.enigma.ws;

public class ClientResponse {
	
	private boolean alreadyRegistered;
	private int livesLeft;
	private int idOnRemoteDB;
	
	public ClientResponse() {}
	
	public ClientResponse(int livesLeft, int idOnRemoteDB) {
		super();
		this.livesLeft = livesLeft;
		this.idOnRemoteDB = idOnRemoteDB;
	}
	
	public boolean isAlreadyRegistered() {
		return alreadyRegistered;
	}
	public void setAlreadyRegistered(boolean alreadyRegistered) {
		this.alreadyRegistered = alreadyRegistered;
	}
	public int getLivesLeft() {
		return livesLeft;
	}
	public void setLivesLeft(int livesLeft) {
		this.livesLeft = livesLeft;
	}
	public int getIdOnRemoteDB() {
		return idOnRemoteDB;
	}
	public void setIdOnRemoteDB(int idOnRemoteDB) {
		this.idOnRemoteDB = idOnRemoteDB;
	}
	
}