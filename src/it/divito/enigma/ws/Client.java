package it.divito.enigma.ws;

import it.divito.enigma.database.UserInfo;
import it.divito.enigma.util.Constants;
import it.divito.enigma.util.Utility;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

public class Client {
	
	private String server;

    public Client(String server) {
        this.server = server;
    }

    private String getBase() {
        return server;
    }
    
    public ClientResponse postBaseURI(UserInfo userInfo, String strUrl) {

    	ClientResponse clientResponse = null;
    	try {
        	JSONObject json = new JSONObject();
        	json.put("imei", userInfo.getImei());
        	json.put("macAddress", userInfo.getMacAddress());
        	json.put("deviceName", userInfo.getDeviceName());
        	json.put("idOnRemoteDB", userInfo.getIdOnRemoteDB());
        	
            HttpParams httpParameters = new BasicHttpParams();
            int timeoutConnection = 3000;
            HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
            int timeoutSocket = 15000;
            HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
            DefaultHttpClient httpClient = new DefaultHttpClient(httpParameters);
            HttpPost postRequest = new HttpPost(getBase() + strUrl);
            
            StringEntity input = new StringEntity(json.toString());
//            input.setContentType("application/json");
//            input.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
            postRequest.setEntity(input);
            postRequest.setHeader("Content-type", "application/json");

            HttpResponse response = httpClient.execute(postRequest);
            httpClient.getConnectionManager().shutdown();
            
            JSONObject jsonResponse  = new JSONObject(getResult(response).toString());
            clientResponse = Utility.parseUserResponse(jsonResponse);
            
    	} catch (JSONException e) {
    		e.printStackTrace();
    		System.out.println("JSONException: " + e.getMessage());
    		clientResponse = Utility.createClientResponseError(e.getMessage());
    	} catch (ClientProtocolException e) {
    		System.out.println("ClientProtocolException: " + e.getMessage());
    		clientResponse = Utility.createClientResponseError(e.getMessage());
    	} catch (IOException e) {
    		e.printStackTrace();
    		System.out.println("IOException: " + e.getMessage());
    		clientResponse = Utility.createClientResponseError(e.getMessage());
    	}
    	
    	return clientResponse;
    }
    
    
    public QuestionResponse getQuestion(String strUrl, int level) {

    	QuestionResponse questionResponse = null;
    	try {
        	JSONObject json = new JSONObject();
        	json.put("userLevel", level);
        	
            HttpParams httpParameters = new BasicHttpParams();
            int timeoutConnection = 3000;
            HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
            int timeoutSocket = 15000;
            HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
            DefaultHttpClient httpClient = new DefaultHttpClient(httpParameters);
            HttpPost postRequest = new HttpPost(getBase() + strUrl);
            
            StringEntity input = new StringEntity(json.toString());
            postRequest.setEntity(input);
            postRequest.setHeader("Content-type", "application/json");

            HttpResponse response = httpClient.execute(postRequest);
            httpClient.getConnectionManager().shutdown();
            
            JSONObject jsonResponse  = new JSONObject(getResult(response).toString());
            questionResponse = Utility.parseQuestionResponse(jsonResponse);
            
    	} catch (JSONException e) {
    		e.printStackTrace();
    		System.out.println("JSONException: " + e.getMessage());
    	} catch (ClientProtocolException e) {
    		System.out.println("ClientProtocolException: " + e.getMessage());
    	} catch (IOException e) {
    		e.printStackTrace();
    		System.out.println("IOException: " + e.getMessage());
    	}
    	
    	return questionResponse;
    }
    
    
    private StringBuilder getResult(HttpResponse response) throws IllegalStateException, IOException {
        StringBuilder result = new StringBuilder();
        BufferedReader br = new BufferedReader(new InputStreamReader((response.getEntity().getContent())), 1024);
        String output;
        while ((output = br.readLine()) != null) 
            result.append(output);

        return result;      
    }

}
