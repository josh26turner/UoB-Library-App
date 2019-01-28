package spe.uoblibraryapp.api.ncip;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v4.app.JobIntentService;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import spe.uoblibraryapp.ActivityConfirm;
import spe.uoblibraryapp.api.IntentActions;


public class WMSNCIPService extends JobIntentService{
    String TAG = "WMSNCIPService";

    private WorkQueue workQueue;


    public void lookupUser(){
        SharedPreferences prefs = getSharedPreferences("userDetails", MODE_PRIVATE);
        String accessToken = prefs.getString("authorisationToken", "");

        String url = "https://bub.share.worldcat.org/ncip/circ-patron";
        String requestBody = "<NCIPMessage xmlns=\"http://www.niso.org/2008/ncip\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:ncip=\"http://www.niso.org/2008/ncip\" xmlns:ns2=\"http://oclc.org/WCL/ncip/2011/extensions\" xsi:schemaLocation=\"http://www.niso.org/2008/ncip http://www.niso.org/schemas/ncip/v2_01/ncip_v2_01.xsd\" ncip:version=\"http://www.niso.org/schemas/ncip/v2_01/ncip_v2_01.xsd\">\n" +
                "<LookupUser>\n" +
                "<InitiationHeader>\n" +
                "<FromAgencyId>\n" +
                "<AgencyId ncip:Scheme=\"http://oclc.org/ncip/schemes/agencyid.scm\">132607</AgencyId>\n" +
                "</FromAgencyId>\n" +
                "<ToAgencyId>\n" +
                "<AgencyId ncip:Scheme=\"http://oclc.org/ncip/schemes/agencyid.scm\">132607</AgencyId>\n" +
                "</ToAgencyId>\n" +
                "<ApplicationProfileType ncip:Scheme=\"http://oclc.org/ncip/schemes/application-profile/wcl.scm\">Version 2011</ApplicationProfileType>\n" +
                "</InitiationHeader>\n" +
                "<UserId>\n" +
                "<UserIdentifierValue>" + prefs.getString("principalID", "") + "</UserIdentifierValue>\n" +
                "</UserId>\n" +
                "<LoanedItemsDesired/>\n" +
                "<RequestedItemsDesired/>\n" +
                "<UserFiscalAccountDesired/>\n" +
                "<Ext>\n" +
                "<ns2:ResponseElementControl>\n" +
                "<ns2:ElementType ncip:Scheme=\"http://worldcat.org/ncip/schemes/v2/extensions/elementtype.scm\">Account Details</ns2:ElementType>\n" +
                "<ns2:StartElement>1</ns2:StartElement>\n" +
                "<ns2:MaximumCount>10</ns2:MaximumCount>\n" +
                "<ns2:SortField ncip:Scheme=\"http://worldcat.org/ncip/schemes/v2/extensions/accountdetailselementtype.scm\">Accrual Date</ns2:SortField>\n" +
                "<ns2:SortOrderType ncip:Scheme=\"http://worldcat.org/ncip/schemes/v2/extensions/sortordertype.scm\">Ascending</ns2:SortOrderType>\n" +
                "</ns2:ResponseElementControl>\n" +
                "<ns2:ResponseElementControl>\n" +
                "<ns2:ElementType ncip:Scheme=\"http://worldcat.org/ncip/schemes/v2/extensions/elementtype.scm\">Loaned Item</ns2:ElementType>\n" +
                "<ns2:StartElement>1</ns2:StartElement>\n" +
                "<ns2:MaximumCount>50</ns2:MaximumCount>\n" +
                "<ns2:SortField ncip:Scheme=\"http://worldcat.org/ncip/schemes/v2/extensions/loaneditemelementtype.scm\">Date Due</ns2:SortField>\n" +
                "<ns2:SortOrderType ncip:Scheme=\"http://worldcat.org/ncip/schemes/v2/extensions/sortordertype.scm\">Ascending</ns2:SortOrderType>\n" +
                "</ns2:ResponseElementControl>\n" +
                "<ns2:ResponseElementControl>\n" +
                "<ns2:ElementType ncip:Scheme=\"http://worldcat.org/ncip/schemes/v2/extensions/elementtype.scm\">Requested Item</ns2:ElementType>\n" +
                "<ns2:StartElement>1</ns2:StartElement>\n" +
                "<ns2:MaximumCount>50</ns2:MaximumCount>\n" +
                "<ns2:SortField ncip:Scheme=\"http://worldcat.org/ncip/schemes/v2/extensions/requesteditemelementtype.scm\">Date Placed</ns2:SortField>\n" +
                "<ns2:SortOrderType ncip:Scheme=\"http://worldcat.org/ncip/schemes/v2/extensions/sortordertype.scm\">Ascending</ns2:SortOrderType>\n" +
                "</ns2:ResponseElementControl>\n" +
                "</Ext>\n" +
                "</LookupUser>\n" +
                "</NCIPMessage>";

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.start();
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String xml) {
                Log.d(TAG, "HTTP request Actioned");

                Intent broadcastIntent = new Intent(IntentActions.USER_PROFILE_RESPONSE);
                broadcastIntent.putExtra("xml", xml);

                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(broadcastIntent);
                Log.d(TAG, "Broadcast Intent Sent");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "ERRROOOOORRRRR");
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + accessToken);
                return headers;
            }

            @Override
            public String getBodyContentType() {
                return "application/xml";
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                try{
                    return requestBody.getBytes("utf-8");
                } catch (UnsupportedEncodingException e){
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8");
                    return null;
                }
            }
        };
        queue.add(request);
    }

    private void checkoutBook(String itemId){
        SharedPreferences prefs = getSharedPreferences("userDetails", MODE_PRIVATE);
        String accessToken = prefs.getString("authorisationToken", "");

        String url = "https://circ.{datacenter}.worldcat.org/ncip";
        String requestBody = "<NCIPMessage xmlns=\"http://www.niso.org/2008/ncip\" xmlns:ncip=\"http://www.niso.org/2008/ncip\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" ncip:version=\"http://www.niso.org/schemas/ncip/v2_01/ncip_v2_01.xsd\" xsi:schemaLocation=\"http://www.niso.org/2008/ncip http://www.niso.org/schemas/ncip/v2_01/ncip_v2_01.xsd\">\n" +
                "<CheckOutItem>\n" +
                "<InitiationHeader>\n" +
                "<FromAgencyId>\n" +
                "<AgencyId ncip:Scheme=\"http://oclc.org/ncip/schemes/agencyid.scm\">132607</AgencyId>\n" +
                "</FromAgencyId>\n" +
                "<ToAgencyId>\n" +
                "<AgencyId>132607</AgencyId>\n" +
                "</ToAgencyId>\n" +
                "<ApplicationProfileType ncip:Scheme=\"http://oclc.org/ncip/schemes/application-profile/platform.scm\">Version 2011</ApplicationProfileType>\n" +
                "</InitiationHeader>\n" +
                "<UserId>\n" +
                "<AgencyId>132607</AgencyId>\n" +
                "<UserIdentifierValue>" + prefs.getString("principalID", "") + "</UserIdentifierValue>\n" +
                "</UserId>\n" +
                "<ItemId>\n" +
                "<AgencyId>132607</AgencyId>\n" +
                "<ItemIdentifierValue>" + itemId + "</ItemIdentifierValue>\n" +
                "</ItemId>\n" +
                "<ItemElementType>Bibliographic Description</ItemElementType>\n" +
                "<ItemElementType>Circulation Status</ItemElementType>\n" +
                "</CheckOutItem>\n" +
                "</NCIPMessage>";


        RequestQueue queue = Volley.newRequestQueue(this);
        queue.start();
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String xml) {
                Log.d(TAG, "HTTP request Actioned");

                Intent confirmIntent = new Intent(getApplicationContext(), ActivityConfirm.class);
                confirmIntent.putExtra("xml" ,xml);
                confirmIntent.setAction(IntentActions.BOOK_CHECK_OUT_RESPONSE);
                startActivity(confirmIntent);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "ERRROOOOORRRRR");
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + accessToken);
                return headers;
            }

            @Override
            public String getBodyContentType() {
                return "application/xml";
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                try{
                    return requestBody.getBytes("utf-8");
                } catch (UnsupportedEncodingException e){
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8");
                    return null;
                }
            }
        };
        queue.add(request);
    }



    @Override
    protected void onHandleWork(@NonNull Intent intent) {
        if (IntentActions.ACCESS_TOKEN_GENERATED.equals(intent.getAction())){
            Log.d(TAG, "token generated intent recieved");
            while (!workQueue.isEmpty()) {
                String action = workQueue.get();
                if (IntentActions.LOOKUP_USER.equals(action)) {
                    lookupUser();
                } else if (IntentActions.CHECKOUT_BOOK.equals((action))) {
                    String itemId = intent.getStringExtra("itemId");
                    checkoutBook(itemId);
                } else {
                    Log.e(TAG, "Intent received has no valid action");
                }
            }
        }else {
            Log.d(TAG, "Intent recieved");
            workQueue.add(intent.getAction());
            Log.d(TAG, "action added to work queue");
            Intent getAccessTokenIntent = new Intent(IntentActions.ACCESS_TOKEN_NEEDED);
            AuthService.enqueueWork(this, AuthService.class, 1001, getAccessTokenIntent);
            Log.d(TAG, "Access Token requested");
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        workQueue = WorkQueue.getInstance();
    }

}
