package spe.uoblibraryapp.api.ncip;

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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import spe.uoblibraryapp.ActivitySignIn;
import spe.uoblibraryapp.SplashScreen;
import spe.uoblibraryapp.api.AuthenticationNeededException;
import spe.uoblibraryapp.api.IntentActions;

public class AuthService extends JobIntentService {
    String TAG = "AuthService";

    private SharedPreferences tokens;

    @Override
    public void onCreate() {
        super.onCreate();
        tokens = getSharedPreferences("userDetails", Context.MODE_PRIVATE);
    }

    @Override
    protected void onHandleWork(@NonNull Intent intent) {
        if(IntentActions.ACCESS_TOKEN_NEEDED.equals(intent.getAction())){
            try {
                getAccessToken();
            } catch (Exception e){
                Log.e(TAG, "Sending auth error", e);
                LocalBroadcastManager.getInstance(this).sendBroadcast(new Intent(IntentActions.AUTH_ERROR));
            }
        }else if (IntentActions.AUTH_LOGOUT.equals(intent.getAction())){
            try {
                logout();
            } catch (Exception e){
                Log.e(TAG, "Sending auth error", e);
                LocalBroadcastManager.getInstance(this).sendBroadcast(new Intent(IntentActions.AUTH_ERROR));
            }
        }
    }

    /**
     * To parse WMS dates into Java Dates
     * @param strDate this is a date from WMS
     * @return This returns the Java Date object from the parsed date
     * @throws ParseException Throws if the date fails to parse
     */
    private Date parseDate(String strDate) throws ParseException{
//        strDate = strDate.replace(" ", " ");
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return format.parse(strDate);
    }
    public static Date addDays(Date date, int days) {
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(date);
        cal.add(Calendar.DATE, days);
        return cal.getTime();
    }

    void getAccessToken() throws ParseException, AuthenticationNeededException, AuthenticationRequestFailedException {
        String accessTokenExpiry = tokens.getString("authorisationTokenExpiry","");
        if (accessTokenExpiry.equals("")){
            startActivity(new Intent(this, ActivitySignIn.class));
            return;
        }
        String accessToken;
        if (parseDate(accessTokenExpiry).before(new Date())){
            requestNewAccessToken();
        } else{
            accessToken = tokens.getString("authorisationToken", "");

            Intent accessTokenGeneratedIntent = new Intent(IntentActions.ACCESS_TOKEN_GENERATED);
            accessTokenGeneratedIntent.putExtra("token", accessToken);
            ConcreteWMSNCIPPatronService.enqueueWork(getApplicationContext(), ConcreteWMSNCIPPatronService.class, 1000, accessTokenGeneratedIntent);
            Log.d(TAG, "Access Token Broadcasted");
        }
    }

    void requestNewAccessToken() throws ParseException, AuthenticationNeededException, AuthenticationRequestFailedException {
        String refreshTokenExpiry = tokens.getString("refreshTokenExpiry", "");
        if (addDays(parseDate(refreshTokenExpiry), -1).before(new Date())){  // Subtracting 1 day to allow for a good overlap period between two active refresh tokens.
            // TODO: Start receiver, wait for authentication, then broadcast intent ACCESS_TOKEN_GENERATED. Then stop receiver.
            startActivity(new Intent(this, ActivitySignIn.class));
        } else {
            String url = "https://authn.sd00.worldcat.org/oauth2/accessToken?grant_type=refresh_token&refresh_token="
                    + tokens.getString("refreshToken", "")
                    + "&client_id=LRQvSrRL1pjZCy8R0AyQpL45QtYvJs6SpjKSF2EmqzmVc0mpIhE85ahM2m4XbByK9qMhl9IcX8fOeOet";
            RequestQueue queue = Volley.newRequestQueue(this);
            queue.start();
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    String accessToken;
                    String accessTokenExpiry;
                    try {
                        accessToken = response.getString("access_token");
                        accessTokenExpiry = response.getString("expires_at");
                    } catch (JSONException e) {
                        // TODO: should this broadcast auth error?
                        return;
                    }

                    tokens.edit().putString("authorisationToken", accessToken).apply();
                    tokens.edit().putString("authorisationTokenExpiry", accessTokenExpiry).apply();


                    Intent accessTokenGeneratedIntent = new Intent(IntentActions.ACCESS_TOKEN_GENERATED);
                    accessTokenGeneratedIntent.putExtra("token", accessToken);
                    ConcreteWMSNCIPPatronService.enqueueWork(getApplicationContext(), ConcreteWMSNCIPPatronService.class, 1000, accessTokenGeneratedIntent);

//                    sendBroadcast(accessTokenGeneratedIntent);

                    Log.d(TAG, "recieved response: broadcast key");
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e(TAG, "ERROR in response");
                }
            });
            Log.d(TAG , "Adding request");
            queue.add(request);

            Log.d(TAG, "Stopping queue");
        }
    }


    void logout(){

        String url = "https://authn.sd00.worldcat.org/oauth2/revoke?refresh_token="
                + tokens.getString("refreshToken", "");
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.start();
        StringRequest request = new StringRequest(Request.Method.PUT, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                tokens.edit().clear().apply();
                startActivity(new Intent(getApplicationContext(), SplashScreen.class));
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // TODO: CRY )`:
            }
        });
        Log.d(TAG , "Adding request");
        queue.add(request);

        Log.d(TAG, "Stopping queue");






        Log.d(TAG, "Logout intent recieved");
    }
}
