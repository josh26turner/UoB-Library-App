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
import org.w3c.dom.NodeList;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import spe.uoblibraryapp.CacheManager;
import spe.uoblibraryapp.Constants;
import spe.uoblibraryapp.api.XMLParser;
import spe.uoblibraryapp.api.wmsobjects.WMSHold;
import spe.uoblibraryapp.api.wmsobjects.WMSUserProfile;

public class CancelReservationRequest extends WMSNCIPAPIRequest {
    private static final String TAG = "CancelReservationRequest";

    private String accessToken;
    private CacheManager cacheManager;

    public CancelReservationRequest(String accessToken){
        url = Constants.APIUrls.patronProfile;
        requestBody = Constants.RequestTemplates.cancelReservation;
        this.accessToken = accessToken;
        cacheManager = CacheManager.getInstance();
    }

    @Override
    StringRequest createRequest(Context context) {
        return new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String xml) {

                String requestId;
                try {
                    Document doc = XMLParser.parse(xml);
                    NodeList nodeList = doc.getElementsByTagName("ns1:RequestIdentifierValue");
                    requestId = nodeList.item(0).getTextContent();
                } catch(Exception ex){
                    requestId = null;
                }

                Log.e(TAG, requestId);


                Intent broadcastIntent;
                if (requestId != null) {
                    // Add parsing stuff here
                    WMSUserProfile userProfile = cacheManager.getUserProfile();
                    Iterator<WMSHold> iterator = userProfile.getOnHold().iterator();
                    while (iterator.hasNext()) {
                        WMSHold p = iterator.next();
                        if (p.getRequestId().equals(requestId)){
                            iterator.remove();
                        }
                    }
                    broadcastIntent = new Intent(Constants.IntentActions.CANCEL_RESERVATION_RESPONSE);
                } else{
                    broadcastIntent = new Intent(Constants.IntentActions.CANCEL_RESERVATION_ERROR);
                }
                LocalBroadcastManager.getInstance(context).sendBroadcast(broadcastIntent);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                LocalBroadcastManager.getInstance(context).sendBroadcast(
                        new Intent(Constants.IntentActions.CANCEL_RESERVATION_ERROR)
                );
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
}
