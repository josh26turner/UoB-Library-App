package spe.uoblibraryapp.api.ncip;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.JobIntentService;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import spe.uoblibraryapp.Constants;
import spe.uoblibraryapp.api.AuthService;


public class WMSNCIPService extends JobIntentService {
    private static final String TAG = "WMSNCIPService";
    public static final Integer jobId = 1000;


    private WorkQueue workQueue;
    private RequestQueue requestQueue;

    public void lookupUser() {
        SharedPreferences prefs = getSharedPreferences("userDetails", MODE_PRIVATE);
        String accessToken = prefs.getString("authorisationToken", "");

        LookupUserRequest lookupUserRequest = new LookupUserRequest(accessToken);
        lookupUserRequest.setRequestBodyParams(
                prefs.getString("principalID", "")
        );

        Log.d(TAG, "request added to queue");
        try {
            requestQueue.add(lookupUserRequest.createRequest(getApplicationContext()));
        } catch (ParamsNotSetException ex){
            Log.e(TAG, "Params not set");
        }
    }




    private void checkoutBook(String itemId) {
        SharedPreferences prefs = getSharedPreferences("userDetails", MODE_PRIVATE);

        CheckoutBookRequest checkoutBookRequest = new CheckoutBookRequest();
        checkoutBookRequest.setRequestBodyParams(
                prefs.getString("principalID", ""),
                prefs.getString("userBarcode", ""),
                prefs.getString("authorisationToken", ""),
                itemId,
                prefs.getString("lastSelectedLocation","")
        );

        Log.d(TAG, "request added to queue");
        try {
            requestQueue.add(checkoutBookRequest.createRequest(getApplicationContext()));
        } catch (ParamsNotSetException ex){
            Log.e(TAG, "Params not set");
        }

    }

    private void cancelReservation(String reservationId, String branchId){
        SharedPreferences prefs = getSharedPreferences("userDetails", MODE_PRIVATE);
        String accessToken = prefs.getString("authorisationToken", "");

        CancelReservationRequest cancelReservationRequest = new CancelReservationRequest(accessToken);
        cancelReservationRequest.setRequestBodyParams(
                branchId,
                prefs.getString("principalID", ""),
                reservationId
        );

        Log.d(TAG, "request added to queue");
        Log.d(TAG, "request added to queue");
        try {
            requestQueue.add(cancelReservationRequest.createRequest(getApplicationContext()));
        } catch (ParamsNotSetException ex){
            Log.e(TAG, "Params not set");
        }

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
                    String reservationId = extras.getString("reservationId");
                    String branchId = extras.getString("branchId");
                    cancelReservation(reservationId, branchId);
                } else {
                    Log.e(TAG, "Intent received has no valid action");
                }
            }
        } else if(Constants.IntentActions.ACCESS_TOKEN_ERROR.equals(intent.getAction())) {
            workQueue.clear();
            sendBroadcast(new Intent(Constants.IntentActions.ACCESS_TOKEN_ERROR));
            LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(new Intent(Constants.IntentActions.ACCESS_TOKEN_ERROR));
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
    }

}
