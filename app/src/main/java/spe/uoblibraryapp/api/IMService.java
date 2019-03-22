package spe.uoblibraryapp.api;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v4.app.JobIntentService;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import spe.uoblibraryapp.Constants;

public class IMService extends JobIntentService {
    private final String TAG = "IMService";
    public final static Integer jobId = 1002;
    private SharedPreferences tokens;
    private RequestQueue requestQueue;


    @Override
    public void onCreate() {
        super.onCreate();
        tokens = getSharedPreferences("userDetails", Context.MODE_PRIVATE);
        requestQueue = Volley.newRequestQueue(this);
    }


    @Override
    protected void onHandleWork(@NonNull Intent intent) {
        Log.e(TAG, "Intent received");
        if (Constants.IntentActions.LOOKUP_USER_ACCOUNT.equals(intent.getAction())) {
            lookupUserAccount();
        }
    }


    /**
     * Will set the users name, email and account status in the userDetails prefs
     */
    private void lookupUserAccount() {
        String userId = tokens.getString("principalID", "");
        String accessToken = tokens.getString("authorisationToken", "");

        if (!"".equals(userId) && !"".equals(accessToken)) {
            // Make new json request
            String url = String.format(Constants.APIUrls.lookupUserAccount, userId);
            JsonRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        String email;
                        try {
                            email = response.getString("email");
                        } catch (JSONException ex){
                            email = "unknown";
                        }
                        String name;
                        try{
                            JSONObject nameObject = response.getJSONObject("name");
                            String firstName = nameObject.getString("givenName");
                            String lastName = nameObject.getString("familyName");
                            name = firstName + " " + lastName;
                        } catch (JSONException ex){
                            name = "unknown";
                        }
                        Boolean accountBlocked;
                        try{
                            JSONObject accountInfo = response.getJSONObject("urn:mace:oclc.org:eidm:schema:persona:wmscircselfinfo:20180101").getJSONObject("circulationInfo");
                            accountBlocked = accountInfo.getBoolean("isCircBlocked");
                        } catch (JSONException ex){
                            ex.printStackTrace();
                            accountBlocked = true;
                        }
                        String borrowerCategory;
                        try {
                            JSONObject accountInfo = response.getJSONObject("urn:mace:oclc.org:eidm:schema:persona:wmscircselfinfo:20180101").getJSONObject("circulationInfo");
                            borrowerCategory = accountInfo.getString("borrowerCategory");
                        } catch (JSONException ex){
                            borrowerCategory = "";
                        }
                        String userBarcode;
                        try {
                            JSONObject accountInfo = response.getJSONObject("urn:mace:oclc.org:eidm:schema:persona:wmscircselfinfo:20180101").getJSONObject("circulationInfo");
                            userBarcode = accountInfo.getString("barcode");
                        } catch (JSONException ex){
                            userBarcode = "";
                        }
                        tokens.edit().putString("name", name).apply();
                        tokens.edit().putString("email", email).apply();
                        tokens.edit().putBoolean("accountBlocked", accountBlocked).apply();
                        tokens.edit().putString("borrowerCategory", borrowerCategory).apply();
                        tokens.edit().putString("userBarcode", userBarcode).apply();
                        sendBroadcast(new Intent(Constants.IntentActions.LOOKUP_USER_ACCOUNT_RESPONSE));
                        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(new Intent(Constants.IntentActions.LOOKUP_USER_ACCOUNT_RESPONSE));
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // todo handle error
                    }
                }
            ){
                @Override
                public Map<String, String> getHeaders() {
                    Map<String, String> headers = new HashMap<>();
                    headers.put("Authorization", accessToken);
                    return headers;
                }
            };

            requestQueue.add(jsonRequest);


            Log.e(TAG, "user response sent");
        }

    }

}
