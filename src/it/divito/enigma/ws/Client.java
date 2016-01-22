package it.divito.enigma.ws;

import it.divito.enigma.util.Utility;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.json.JSONObject;

import android.util.Log;

public class Client {
	
	private String server;

    public Client(String server) {
        this.server = server;
    }

    private String getBase() {
        return server;
    }
    
    public ClientResponse postBaseURI(String imei, String mac, String deviceName, String strUrl) {
        /*
        ArrayList<NameValuePair> n = new ArrayList<NameValuePair>();
        n.add(new BasicNameValuePair("imei", imei));
        n.add(new BasicNameValuePair("mac", mac));
        */
        try {
        	JSONObject json = new JSONObject();
        	json.put("imei", imei);
        	json.put("macAddress", mac);
        	json.put("deviceName", deviceName);
        	
            HttpParams httpParameters = new BasicHttpParams();
            int timeoutConnection = 3000;
            HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
            int timeoutSocket = 5000;
            HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
            DefaultHttpClient httpClient = new DefaultHttpClient(httpParameters);
            HttpPost postRequest = new HttpPost(getBase() + strUrl);
            
            StringEntity input = new StringEntity(json.toString(), HTTP.UTF_8);
            input.setContentType("application/json");
            input.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
            postRequest.setEntity(input);

            HttpResponse response = httpClient.execute(postRequest);
            httpClient.getConnectionManager().shutdown();
            
            JSONObject jsonResponse = new JSONObject(getResult(response).toString());
            return Utility.parseWSResponse(jsonResponse);
            
        } catch (Exception e) {
        	e.printStackTrace();
            System.out.println(e.getMessage());
        }
        
        return null;
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
