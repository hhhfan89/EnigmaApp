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
	
	public static ClientResponse parseWSResponse(JSONObject jsonResponse) throws JSONException {
		ClientResponse clientResponse = new ClientResponse();
		if(jsonResponse!=null) {
			clientResponse.setAlreadyRegistered(Boolean.parseBoolean((String) jsonResponse.get("alreadyRegistered")));
			clientResponse.setLivesLeft(Integer.parseInt((String) jsonResponse.get("livesLeft")));
			clientResponse.setIdOnRemoteDB(Integer.parseInt((String) jsonResponse.get("idOnRemoteDB")));
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
}
