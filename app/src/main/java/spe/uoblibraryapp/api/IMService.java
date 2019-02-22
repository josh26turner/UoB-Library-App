package spe.uoblibraryapp.api;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v4.app.JobIntentService;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

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
            Log.e(TAG, "IFFFF");
            lookupUserAccount();
        }
    }


    /**
     * Will set the users name, email and account status in the userDetails prefs
     */
    private void lookupUserAccount(){
        String userId = tokens.getString("principalID","");
        String userIdNS = tokens.getString("principalIDNS","aa");
        
        if (!"".equals(userId) && !"".equals(userIdNS)){
            // Make new json request
            tokens.edit().putString("name", "Riley Evans").apply();
            tokens.edit().putString("email", "riley.evans@bristol.ac.uk").apply();
            tokens.edit().putBoolean("accountBlocked", false).apply();
            LocalBroadcastManager.getInstance(this).sendBroadcast(new Intent(Constants.IntentActions.LOOKUP_USER_ACCOUNT_RESPONSE));
            Log.e(TAG, "user response sent");
        }

    }

}
