package it.divito.enigma.util;

import it.divito.enigma.ws.ClientResponse;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.widget.Toast;

public class Utility {

	public static void showToast(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
    }
	
	public static ClientResponse parseWSResponse(JSONObject jsonResponse) {
		ClientResponse clientResponse = new ClientResponse();
		try {
			clientResponse.setAlreadyRegistered(Boolean.parseBoolean((String) jsonResponse.get("alreadyRegistered")));
			clientResponse.setLivesLeft(Integer.parseInt((String) jsonResponse.get("livesLeft")));
			clientResponse.setIdOnRemoteDB(Integer.parseInt((String) jsonResponse.get("idOnRemoteDB")));
		} catch (JSONException e) {
			
		}
        return clientResponse;
	}
}
