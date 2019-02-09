package spe.uoblibraryapp.api.ncip;

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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import spe.uoblibraryapp.ActivityConfirm;
import spe.uoblibraryapp.Constants;
import spe.uoblibraryapp.api.AuthService;


public class WMSNCIPService extends JobIntentService {
    private static final String TAG = "WMSNCIPService";
    public static final Integer jobId = 1000;

    private WorkQueue workQueue;


    public void lookupUser() {
        SharedPreferences prefs = getSharedPreferences("userDetails", MODE_PRIVATE);
        String accessToken = prefs.getString("authorisationToken", "");

        String url = Constants.APIUrls.lookupUser;
        String requestBody = String.format(
                Constants.RequestTemplates.lookupUser,
                prefs.getString("principalID", ""));

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.start();
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String xml) {
                Log.d(TAG, "HTTP request Actioned");

                Intent broadcastIntent = new Intent(Constants.IntentActions.USER_PROFILE_RESPONSE);
                broadcastIntent.putExtra("xml", xml);

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
        queue.add(request);
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
                accessToken,
                itemId);

        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String xml) {
                Log.d(TAG, "HTTP request Actioned");

                Intent confirmIntent = new Intent(getApplicationContext(), ActivityConfirm.class);
                confirmIntent.putExtra("xml", xml);
                confirmIntent.setAction(Constants.IntentActions.BOOK_CHECK_OUT_RESPONSE);
                startActivity(confirmIntent);

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

    private void mockCheckOutBook() {
        SharedPreferences prefs = getSharedPreferences("userDetails", MODE_PRIVATE);
        String response = "<ns1:NCIPMessage xmlns:ns2=\"http://oclc.org/WCL/ncip/2011/extensions\" xmlns:ns1=\"http://www.niso.org/2008/ncip\" ns1:version=\"2.0\">\n" +
                "<ns1:CheckOutItemResponse>\n" +
                "<ns1:ItemId>\n" +
                "<ns1:AgencyId>132607</ns1:AgencyId>\n" +
                "<ns1:ItemIdentifierValue>151468622X</ns1:ItemIdentifierValue>\n" +
                "</ns1:ItemId>\n" +
                "<ns1:UserId>\n" +
                "<ns1:AgencyId>132607</ns1:AgencyId>\n" +
                "<ns1:UserIdentifierValue>" + prefs.getString("principalID", "") + "</ns1:UserIdentifierValue>\n" +
                "</ns1:UserId>\n" +
                "<ns1:DateDue>2019-02-04T23:59:59Z</ns1:DateDue>\n" +
                "<ns1:RenewalCount>1</ns1:RenewalCount>\n" +
                "<ns1:ItemOptionalFields>\n" +
                "<ns1:BibliographicDescription>\n" +
                "<ns1:Author>Julie Schumacher</ns1:Author>\n" +
                "<ns1:BibliographicRecordId>\n" +
                "<ns1:BibliographicRecordIdentifier>959276078</ns1:BibliographicRecordIdentifier>\n" +
                "<ns1:BibliographicRecordIdentifierCode ns1:Scheme=\"http://www.niso.org/ncip/v2_0/schemes/bibliographicrecordidentifiercode/bibliographicrecordidentifiercode.scm\">OCLC</ns1:BibliographicRecordIdentifierCode>\n" +
                "</ns1:BibliographicRecordId>\n" +
                "<ns1:PublicationDate>2017</ns1:PublicationDate>\n" +
                "<ns1:Publisher>Chicago, Ill. : University of Chicago Press,</ns1:Publisher>\n" +
                "<ns1:Title>Doodling for academics : a coloring and activity book /</ns1:Title>\n" +
                "<ns1:Language ns1:Scheme=\"http://lcweb.loc.gov/standards/iso639-2/bibcodes.html\">eng</ns1:Language>\n" +
                "<ns1:MediumType>Book</ns1:MediumType>\n" +
                "</ns1:BibliographicDescription>\n" +
                "</ns1:ItemOptionalFields>\n" +
                "</ns1:CheckOutItemResponse>\n" +
                "</ns1:NCIPMessage>";

        try {
            Thread.sleep(1500);
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
        Intent confirmIntent = new Intent(getApplicationContext(), ActivityConfirm.class);
        confirmIntent.putExtra("xml", response);
        confirmIntent.setAction(Constants.IntentActions.BOOK_CHECK_OUT_RESPONSE);
        startActivity(confirmIntent);
    }


    @Override
    protected void onHandleWork(@NonNull Intent intent) {
        if (Constants.IntentActions.ACCESS_TOKEN_GENERATED.equals(intent.getAction())) {
            Log.d(TAG, "token generated intent recieved");
            while (!workQueue.isEmpty()) {
                String action = workQueue.get();
                if (Constants.IntentActions.LOOKUP_USER.equals(action)) {
                    lookupUser();
                } else if (Constants.IntentActions.CHECKOUT_BOOK.equals((action))) {
//                    String itemId = intent.getStringExtra("itemId"); // Use this
                    mockCheckOutBook(); // Comment this out once the server is live
//                    checkoutBook(itemId); // And use this one
                } else {
                    Log.e(TAG, "Intent received has no valid action");
                }
            }
        } else {
            Log.d(TAG, "Intent recieved");
            workQueue.add(intent.getAction());
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
    }

}
