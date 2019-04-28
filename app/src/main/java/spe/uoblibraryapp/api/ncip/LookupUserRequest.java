package spe.uoblibraryapp.api.ncip;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;

import spe.uoblibraryapp.CacheManager;
import spe.uoblibraryapp.Constants;
import spe.uoblibraryapp.api.WMSException;
import spe.uoblibraryapp.api.WMSResponse;
import spe.uoblibraryapp.api.wmsobjects.WMSParseException;
import spe.uoblibraryapp.api.wmsobjects.WMSUserProfile;

public class LookupUserRequest extends WMSNCIPAPIRequest {

    private static final String TAG = "LookupUserRequest";

    private String accessToken;
    private CacheManager cacheManager;

    public LookupUserRequest(String accessToken){
        url = Constants.APIUrls.patronProfile;
        requestBody = Constants.RequestTemplates.lookupUser;
        this.accessToken = accessToken;
        cacheManager = CacheManager.getInstance();
    }

    @Override
    StringRequest createRequest(Context context) throws ParamsNotSetException{
        super.createRequest(context);
        return new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String xml) {
                Log.d(TAG, "HTTP request Actioned");
                Log.e(TAG, xml);
                // Add parsing stuff here
                WMSUserProfile userProfile;

                try {
                    userProfile = parseUserProfileResponse(xml);
                } catch (Exception ex){
                    userProfile = null;
                }

                Intent broadcastIntent;
                if (userProfile != null) {
                    // Update cache
                    cacheManager.setUserProfile(userProfile);
                    broadcastIntent = new Intent(Constants.IntentActions.LOOKUP_USER_RESPONSE);
                } else {
                    Log.e(TAG, "Error");
                    broadcastIntent = new Intent(Constants.IntentActions.LOOKUP_USER_ERROR);
                }
                LocalBroadcastManager.getInstance(context).sendBroadcast(broadcastIntent);
                Log.d(TAG, "Broadcast Intent Sent");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent(Constants.IntentActions.LOOKUP_USER_ERROR));
            }
        }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + accessToken);
                return headers;
            }

            @Override
            public String getBodyContentType() {
                return "application/xml";
            }

            @Override
            public byte[] getBody() {
                return requestBody.getBytes(StandardCharsets.UTF_8);
            }
        };
    }

    private WMSUserProfile parseUserProfileResponse(String xml) throws WMSException, WMSParseException {
        WMSResponse response = new WMSNCIPResponse(xml);

        if (response.didFail()) {
            throw new WMSException("There was an error retrieving the User Profile");
        }
        Document doc;
        try {
            doc = response.parse();
        } catch (IOException | SAXException | ParserConfigurationException e) {
            throw new WMSException("There was an error Parsing the WMS response");
        }
        Node node = doc.getElementsByTagName("ns1:LookupUserResponse").item(0);
        return new WMSUserProfile(new WMSNCIPElement(node));
    }
}
