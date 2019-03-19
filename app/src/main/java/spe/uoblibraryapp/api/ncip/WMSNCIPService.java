package spe.uoblibraryapp.api.ncip;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.JobIntentService;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;

import spe.uoblibraryapp.CacheManager;
import spe.uoblibraryapp.Constants;
import spe.uoblibraryapp.api.AuthService;
import spe.uoblibraryapp.api.WMSException;
import spe.uoblibraryapp.api.WMSResponse;
import spe.uoblibraryapp.api.XMLParser;
import spe.uoblibraryapp.api.wmsobjects.WMSHold;
import spe.uoblibraryapp.api.wmsobjects.WMSLoan;
import spe.uoblibraryapp.api.wmsobjects.WMSParseException;
import spe.uoblibraryapp.api.wmsobjects.WMSUserProfile;


public class WMSNCIPService extends JobIntentService {
    private static final String TAG = "WMSNCIPService";
    public static final Integer jobId = 1000;


    private WorkQueue workQueue;
    private CacheManager cacheManager;
    private RequestQueue requestQueue;

    public void lookupUser() {
        SharedPreferences prefs = getSharedPreferences("userDetails", MODE_PRIVATE);
        String accessToken = prefs.getString("authorisationToken", "");

        String url = Constants.APIUrls.patronProfile;
        String requestBody = String.format(
                Constants.RequestTemplates.lookupUser,
                prefs.getString("principalID", ""));


        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String xml) {
                Log.d(TAG, "HTTP request Actioned");


                // Add parsing stuff here
                WMSUserProfile userProfile;

                try {
                    userProfile = parseUserProfileResponse(xml);
                } catch (Exception ex){
                    userProfile = null;
                }

                Intent broadcastIntent;
                if (userProfile != null) {
                    for (WMSLoan loan : userProfile.getLoans()){
                        loan.fetchIsRenewable(getApplicationContext());
                    }

                    // Update cache
                    cacheManager.setUserProfile(userProfile);

                    broadcastIntent = new Intent(Constants.IntentActions.USER_PROFILE_RESPONSE);
                } else {
                    broadcastIntent = new Intent(Constants.IntentActions.LOOKUP_USER_ERROR);
                }
                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(broadcastIntent);
                Log.d(TAG, "Broadcast Intent Sent");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "ERRROOOOORRRRR");
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
        requestQueue.add(request);
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


    private void checkoutBook(String itemId) {
        SharedPreferences prefs = getSharedPreferences("userDetails", MODE_PRIVATE);
        String accessToken = prefs.getString("authorisationToken", "");

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.start();

        String url = Constants.APIUrls.checkoutBook;
        String requestBody = String.format(
                Constants.RequestTemplates.checkoutBook,
                prefs.getString("principalID", ""),
                prefs.getString("userBarcode", ""),
                accessToken,
                itemId,
                prefs.getString("lastSelectedLocation","")
        );

        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String xml) {
                Log.d(TAG, "HTTP request Actioned");
//                Intent confirmIntent = new Intent(getApplicationContext(), ActivityConfirm.class);
//                confirmIntent.putExtra("xml", xml);
//                confirmIntent.setAction(Constants.IntentActions.BOOK_CHECK_OUT_RESPONSE);
//                startActivity(confirmIntent);

                Intent intent = new Intent(Constants.IntentActions.BOOK_CHECK_OUT_RESPONSE);
                intent.putExtra("xml", xml);
                sendBroadcast(intent);
                Log.d(TAG, "broadcast sent");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "ERRROOOOORRRRR");
            }
        }) {
            @Override
            public String getBodyContentType() {
                return "application/xml";
            }

            @Override
            public byte[] getBody() {
                return requestBody.getBytes(StandardCharsets.UTF_8);
            }
        };
        queue.add(request);
    }

    private void cancelReservation(String reservationId, String branchId){
        SharedPreferences prefs = getSharedPreferences("userDetails", MODE_PRIVATE);
        String accessToken = prefs.getString("authorisationToken", "");

        String url = Constants.APIUrls.patronProfile;
        String requestBody = String.format(
                Constants.RequestTemplates.cancelReservation,
                branchId,
                prefs.getString("principalID", ""),
                reservationId);
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String xml) {
                Log.d(TAG, "HTTP request Actioned");

                String requestId;
                try {
                    Document doc = XMLParser.parse(xml);
                    Log.d(TAG,xml);
                    NodeList nodeList = doc.getElementsByTagName("ns1:RequestIdentifierValue");
                    Log.d(TAG, "nodelist" + String.valueOf(nodeList.getLength()));
                    requestId = nodeList.item(0).getTextContent();
                    Log.d(TAG, "done");
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
                            Log.e(TAG, "request removed");
                        }
                    }
                    broadcastIntent = new Intent(Constants.IntentActions.CANCEL_RESERVATION_RESPONSE);
                } else{
                    Log.d(TAG, "Error");
                    broadcastIntent = new Intent(Constants.IntentActions.CANCEL_RESERVATION_ERROR);
                }
                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(broadcastIntent);
                Log.d(TAG, "Broadcast Intent Sent");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "ERRROOOOORRRRR");
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
        requestQueue.add(request);
    }

    @Override
    protected void onHandleWork(@NonNull Intent intent) {
        if (Constants.IntentActions.ACCESS_TOKEN_GENERATED.equals(intent.getAction())) {
            Log.d(TAG, "token generated intent received");
            while (!workQueue.isEmpty()) {
                WorkQueue.WorkQueueObject workQueueObject = workQueue.get();
                String action = workQueueObject.getAction();
                Bundle extras = workQueueObject.getExtras();
                if (Constants.IntentActions.LOOKUP_USER.equals(action)) {
                    lookupUser();
                } else if (Constants.IntentActions.CHECKOUT_BOOK.equals(action)) {
                    String itemId = extras.getString("itemId");
                    checkoutBook(itemId);
                } else if(Constants.IntentActions.CANCEL_RESERVATION.equals(action)) {
                    for(String key : extras.keySet()){
                        Log.e(TAG, key);
                    }
                    String reservationId = extras.getString("reservationId");
                    String branchId = extras.getString("branchId");
                    if (branchId != null) Log.e(TAG, branchId);
                    else Log.e(TAG, "Branch id is null");
                    cancelReservation(reservationId, branchId);
                } else {
                    Log.e(TAG, "Intent received has no valid action");
                }
            }
        } else {
            Log.d(TAG, "Intent received");
            workQueue.add(intent.getAction(), intent.getExtras());
            Log.d(TAG, "action added to work queue");
            Intent getAccessTokenIntent = new Intent(Constants.IntentActions.ACCESS_TOKEN_NEEDED);
            AuthService.enqueueWork(this, AuthService.class, AuthService.jobId, getAccessTokenIntent);
            Log.d(TAG, "Access Token requested");
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        workQueue = WorkQueue.getInstance();
        requestQueue = Volley.newRequestQueue(this);
        requestQueue.start();
        cacheManager = CacheManager.getInstance();
    }

}
