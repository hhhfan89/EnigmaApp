package it.divito.enigma.util;

import java.io.File;

public interface Constants {

	// DB Const
	public static final String DATABASE_NAME = "alessioapp";
	public static final int DATABASE_VERSION = 1;
	public static final String TABLE_NAME = "USERS";
	public static final String COLUMN_ID = "_ID";
	public static final String COLUMN_IMEI = "IMEI";
	public static final String COLUMN_DEVICE_NAME = "DEVICE_NAME";
	public static final String COLUMN_MAC = "MAC";
	public static final String COLUMN_LIVES = "LIVES";
	public static final String COLUMN_ID_ON_REMOTE_DB = "ID_ON_REMOTE_DB";
	
	public static final String NOT_AVAILABLE = "NOT_AVAILABLE";
	public static final String IMEI_NUMBER = "imeiNumber";
	public static final String DEVICE_NAME = "deviceName";
	public static final String MAC_ADDRESS = "macAddress";
	
	public static final int DEFAULT_LIVES_LEFT = 1;
	public static final int DEFAULT_ID_ON_REMOTE_DB = 0;

	// Remote Webapp params
	public static final String WS_HOST = "http://enigmawebappopenshift-enigmawebapp.rhcloud.com";
	public static final String WS_LOCALHOST = "http://localhost:8080";
	public static final String WS_APP_NAME = "Prova";
	
	public static final String WS_OPERATION_SAVE_USER = File.separator + "users" + File.separator + "saveUser";
	public static final String WS_OPERATION_CHECK_USER = File.separator + "users" + File.separator + "checkUser";
	
	
	
}
