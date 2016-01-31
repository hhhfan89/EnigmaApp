package it.divito.enigma.database;

public class UserInfo {
	
	private String imei;
	private String macAddress;
	private String deviceName;
	private int livesLeft;
	private long idOnRemoteDB;
	
	public UserInfo() {}
	
	public UserInfo(int livesLeft, long idOnRemoteDB) {
		this.livesLeft = livesLeft;
		this.idOnRemoteDB = idOnRemoteDB;
	}
	
	public int getLivesLeft() {
		return livesLeft;
	}
	public void setLivesLeft(int livesLeft) {
		this.livesLeft = livesLeft;
	}
	public long getIdOnRemoteDB() {
		return idOnRemoteDB;
	}
	public void setIdOnRemoteDB(long idOnRemoteDB) {
		this.idOnRemoteDB = idOnRemoteDB;
	}

	public String getImei() {
		return imei;
	}

	public void setImei(String imei) {
		this.imei = imei;
	}

	public String getMacAddress() {
		return macAddress;
	}

	public void setMacAddress(String macAddress) {
		this.macAddress = macAddress;
	}

	public String getDeviceName() {
		return deviceName;
	}

	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}
	
}