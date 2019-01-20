package spe.uoblibraryapp.api.ncip;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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

import spe.uoblibraryapp.FragmentLoans;
import spe.uoblibraryapp.api.IntentActions;
import spe.uoblibraryapp.api.WMSResponse;


public class ConcreteWMSNCIPPatronService extends JobIntentService implements WMSNCIPPatronService {
    String TAG = "ConcreteWMSNCIPPatronService";

    private WorkQueue workQueue;
    private MyBroadCastReceiver myBroadCastReceiver;


    @Override
    public void lookup_user(){
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
                "<ns2:MaximumCount>10</ns2:MaximumCount>\n" +
                "<ns2:SortField ncip:Scheme=\"http://worldcat.org/ncip/schemes/v2/extensions/loaneditemelementtype.scm\">Date Due</ns2:SortField>\n" +
                "<ns2:SortOrderType ncip:Scheme=\"http://worldcat.org/ncip/schemes/v2/extensions/sortordertype.scm\">Ascending</ns2:SortOrderType>\n" +
                "</ns2:ResponseElementControl>\n" +
                "<ns2:ResponseElementControl>\n" +
                "<ns2:ElementType ncip:Scheme=\"http://worldcat.org/ncip/schemes/v2/extensions/elementtype.scm\">Requested Item</ns2:ElementType>\n" +
                "<ns2:StartElement>1</ns2:StartElement>\n" +
                "<ns2:MaximumCount>10</ns2:MaximumCount>\n" +
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

                Intent broadcastIntent = new Intent(FragmentLoans.BROADCAST_ACTION);
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

    @Override
    public WMSResponse renew_item(String book_id) {
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n"
                + "<ns1:NCIPMessage xmlns:ns1=\"http://www.niso.org/2008/ncip\" xmlns=\"http://oclc.org/WCL/ncip/2011/extensions\" xmlns:ns3=\"http://www.oclc.org/ncip/usernote/2012\">\n"
                + "<ns1:RenewItemResponse>\n"
                + "<ns1:ItemId>\n"
                + "<ns1:ItemIdentifierValue>eec21bc3-6af3-4b9f-ac1c-1066c95e7734</ns1:ItemIdentifierValue>\n"
                + "</ns1:ItemId>" + "<ns1:DateDue>2014-03-22T03:59:59Z</ns1:DateDue>\n"
                + "<ns1:RenewalCount>1</ns1:RenewalCount>" + "</ns1:RenewItemResponse>" + "</ns1:NCIPMessage>\n";
        return create_response(xml);
    }

    @Override
    public WMSResponse renew_all() {
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n" +
                "<ns2:NCIPMessage xmlns=\"http://oclc.org/WCL/ncip/2011/extensions\"\n" +
                "xmlns:ns2=\"http://www.niso.org/2008/ncip\" xmlns:ns3=\"http://www.oclc.org/ncip/renewall/2014\"\n" +
                "xmlns:ns4=\"http://www.oclc.org/ncip/usernote/2012\"\n" +
                "ns2:version=\"http://www.niso.org/schemas/ncip/v2_0/imp1/xsd/ncip_v2_01.xsd\">\n" +
                "<ns2:Ext>\n" +
                "<ns3:RenewAllItemResponse>\n" +
                "<ns2:UserId>\n" +
                "<ns2:AgencyId ns2:Scheme=\"http://oclc.org/ncip/schemes/agencyid/institutionid.scm\"\n" +
                ">128807</ns2:AgencyId>\n" +
                "<ns2:UserIdentifierValue>26484a07-89z4-44y6-81b1-3147i629995m</ns2:UserIdentifierValue>\n" +
                "</ns2:UserId>\n" +
                "<ns3:RenewInformation>\n" +
                "<ns2:ItemId>\n" +
                "<ns2:AgencyId ns2:Scheme=\"http://oclc.org/ncip/schemes/agencyid.scm\"\n" +
                ">128807</ns2:AgencyId>\n" +
                "<ns2:ItemIdentifierValue>684b2138-8b7a-473a-bc46-175990a1092c</ns2:ItemIdentifierValue>\n" +
                "</ns2:ItemId>\n" +
                "<ns2:DateDue>2015-01-20T04:59:59Z</ns2:DateDue>\n" +
                "<ns2:RenewalCount>7</ns2:RenewalCount>\n" +
                "</ns3:RenewInformation>\n" +
                "<ns3:RenewInformation>\n" +
                "<ns2:ItemId>\n" +
                "<ns2:AgencyId ns2:Scheme=\"http://oclc.org/ncip/schemes/agencyid.scm\"\n" +
                ">128807</ns2:AgencyId>\n" +
                "<ns2:ItemIdentifierValue>6bb56325-1f14-4dc4-8561-a8fa35d52853</ns2:ItemIdentifierValue>\n" +
                "</ns2:ItemId>\n" +
                "<ns2:DateDue>2015-01-20T04:59:59Z</ns2:DateDue>\n" +
                "<ns2:RenewalCount>7</ns2:RenewalCount>\n" +
                "</ns3:RenewInformation>\n" +
                "<ns3:RenewInformation>\n" +
                "<ns2:ItemId>\n" +
                "<ns2:AgencyId ns2:Scheme=\"http://oclc.org/ncip/schemes/agencyid.scm\"\n" +
                ">128807</ns2:AgencyId>\n" +
                "<ns2:ItemIdentifierValue>17825536-0b23-4838-b1da-1eb990ce71bc</ns2:ItemIdentifierValue>\n" +
                "</ns2:ItemId>\n" +
                "<ns2:DateDue>2015-01-20T04:59:59Z</ns2:DateDue>\n" +
                "<ns2:RenewalCount>5</ns2:RenewalCount>\n" +
                "</ns3:RenewInformation>\n" +
                "<ns3:RenewInformation>\n" +
                "<ns2:ItemId>\n" +
                "<ns2:AgencyId ns2:Scheme=\"http://oclc.org/ncip/schemes/agencyid.scm\"\n" +
                ">128807</ns2:AgencyId>\n" +
                "<ns2:ItemIdentifierValue>62c4902b-f583-491d-923e-00eca8a8b54b</ns2:ItemIdentifierValue>\n" +
                "</ns2:ItemId>\n" +
                "<ns2:DateDue>2015-01-20T04:59:59Z</ns2:DateDue>\n" +
                "<ns2:RenewalCount>8</ns2:RenewalCount>\n" +
                "</ns3:RenewInformation>\n" +
                "<ns3:RenewInformation>\n" +
                "<ns2:ItemId>\n" +
                "<ns2:AgencyId ns2:Scheme=\"http://oclc.org/ncip/schemes/agencyid.scm\"\n" +
                ">128807</ns2:AgencyId>\n" +
                "<ns2:ItemIdentifierValue>aff043b3-405b-4e18-975b-9f08b96686a7</ns2:ItemIdentifierValue>\n" +
                "</ns2:ItemId>\n" +
                "<ns2:DateDue>2015-02-12T04:59:59Z</ns2:DateDue>\n" +
                "<ns2:RenewalCount>1</ns2:RenewalCount>\n" +
                "</ns3:RenewInformation>\n" +
                "</ns3:RenewAllItemResponse>\n" +
                "</ns2:Ext>\n" +
                "</ns2:NCIPMessage>\n";
        return create_response(xml);
    }

    @Override
    public WMSResponse request_item() {
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n" +
                "<ns1:NCIPMessage xmlns:ns1=\"http://www.niso.org/2008/ncip\" xmlns=\"http://oclc.org/WCL/ncip/2011/extensions\" xmlns:ns3=\"http://www.oclc.org/ncip/usernote/2012\">\n" +
                "<ns1:RequestItemResponse>\n" +
                "<ns1:RequestId>" +
                "<ns1:AgencyId>128807</ns1:AgencyId>\n" +
                "<ns1:RequestIdentifierValue>bc219183-b3a8-4d5b-878d-797a8dff95ec</ns1:RequestIdentifierValue>\n" +
                "</ns1:RequestId>\n" +
                "<ns1:UserId>\n" +
                "<ns1:UserIdentifierType>EIDM</ns1:UserIdentifierType>\n" +
                "<ns1:UserIdentifierValue>998c3f4b-765e-48b2-asdf-8bd2cf8acee9</ns1:UserIdentifierValue>\n" +
                "</ns1:UserId>\n" +
                "<ns1:RequestType ns1:Scheme=\"http://www.niso.org/ncip/v1_0/imp1/schemes/requesttype/requesttype.scm\">Hold</ns1:RequestType>\n" +
                "<ns1:RequestScopeType ns1:Scheme=\"http://www.niso.org/ncip/v1_0/imp1/schemes/requestscopetype/requestscopetype.scm\">Bibliographic Item</ns1:RequestScopeType>\n" +
                "<ns1:ItemOptionalFields>\n" +
                "<ns1:BibliographicDescription>\n" +
                "<ns1:Author>Staikos, K.</ns1:Author>\n" +
                "<ns1:BibliographicRecordId>\n" +
                "<ns1:BibliographicRecordIdentifier>43286419</ns1:BibliographicRecordIdentifier>\n" +
                "<ns1:BibliographicRecordIdentifierCode ns1:Scheme=\"http://www.niso.org/ncip/v1_0/imp1/schemes/bibliographicrecordidentifiercode/bibliographicrecordidentifiercode.scm\">OCLC</ns1:BibliographicRecordIdentifierCode>\n" +
                "</ns1:BibliographicRecordId>\n" +
                "<ns1:Edition>1st English ed.</ns1:Edition>\n" +
                "<ns1:PublicationDate>2000</ns1:PublicationDate>\n" +
                "<ns1:Publisher>London : The British Library,</ns1:Publisher>\n" +
                "<ns1:Title>The great libraries : from antiquity to the Renaissance (3000 B.C. to A.D. 1600) /</ns1:Title>\n" +
                "<ns1:Language ns1:Scheme=\"http://lcweb.loc.gov/standards/iso639-2/bibcodes.html\">eng</ns1:Language>\n" +
                "<ns1:MediumType ns1:Scheme=\"http://worldcat.org/ncip/schemes/v2/extensions/mediumtype.scm\">Book</ns1:MediumType>\n" +
                "</ns1:BibliographicDescription>\n" +
                "<ns1:HoldQueueLength>1</ns1:HoldQueueLength>\n" +
                "</ns1:ItemOptionalFields>\n" +
                "<ns1:Ext>\n" +
                "<ns1:HoldQueuePosition>1</ns1:HoldQueuePosition>\n" +
                "</ns1:Ext>\n" +
                "</ns1:RequestItemResponse>\n" +
                "</ns1:NCIPMessage>\n";
        return create_response(xml);
    }

    @Override
    public WMSResponse request_bibliographic() {
        // TODO: Add mock api response
        return null;
    }

    @Override
    public WMSResponse update_request() {
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n" +
                "<ns1:NCIPMessage xmlns:ns1=\"http://www.niso.org/2008/ncip\" xmlns=\"http://oclc.org/WCL/ncip/2011/extensions\" xmlns:ns3=\"http://www.oclc.org/ncip/usernote/2012\">\n" +
                "<ns1:UpdateRequestItemResponse>\n" +
                "<ns1:ItemId>\n" +
                "<ns1:ItemIdentifierValue/>\n" +
                "</ns1:ItemId>\n" +
                "<ns1:UserId>\n" +
                "<ns1:UserIdentifierType>EIDM</ns1:UserIdentifierType>\n" +
                "<ns1:UserIdentifierValue>998c3f4b-765e-48b2-asdf-8bd2cf8acee9</ns1:UserIdentifierValue>\n" +
                "</ns1:UserId>\n" +
                "<ns1:RequestType ns1:Scheme=\"http://www.niso.org/ncip/v1_0/imp1/schemes/requesttype/requesttype.scm\">Hold</ns1:RequestType>\n" +
                "<ns1:RequestScopeType ns1:Scheme=\"http://www.niso.org/ncip/v1_0/imp1/schemes/requestscopetype/requestscopetype.scm\">Bibliographic Item</ns1:RequestScopeType>\n" +
                "</ns1:UpdateRequestItemResponse>\n" +
                "</ns1:NCIPMessage>\n";
        return create_response(xml);
    }

    @Override
    public WMSResponse cancel_request() {
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n" +
                "<ns1:NCIPMessage xmlns:ns1=\"http://www.niso.org/2008/ncip\" xmlns=\"http://oclc.org/WCL/ncip/2011/extensions\" xmlns:ns3=\"http://www.oclc.org/ncip/usernote/2012\">\n" +
                "<ns1:CancelRequestItemResponse>\n" +
                "<ns1:RequestId>\n" +
                "<ns1:AgencyId>128807</ns1:AgencyId>\n" +
                "<ns1:RequestIdentifierValue>bc219183-b3a8-4d5b-878d-797a8dff95ec</ns1:RequestIdentifierValue>\n" +
                "</ns1:RequestId>\n" +
                "<ns1:UserId>\n" +
                "<ns1:UserIdentifierType>EIDM</ns1:UserIdentifierType>\n" +
                "<ns1:UserIdentifierValue>998c3f4b-765e-48b2-asdf-8bd2cf8acee9</ns1:UserIdentifierValue>\n" +
                "</ns1:UserId>\n" +
                "</ns1:CancelRequestItemResponse>\n" +
                "</ns1:NCIPMessage>\n";
        return create_response(xml);
    }

    private WMSResponse create_response(String xml){
        return new WMSNCIPResponse(xml);
    }


    @Override
    protected void onHandleWork(@NonNull Intent intent) {
        if (IntentActions.ACCESS_TOKEN_GENERATED.equals(intent.getAction())){
            Log.d(TAG, "token generated intent recieved");
            while (!workQueue.isEmpty()) {
                String action = workQueue.get();
                if (IntentActions.LOOKUP_USER.equals(action)) {
                    lookup_user();
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
        myBroadCastReceiver = new MyBroadCastReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(IntentActions.ACCESS_TOKEN_GENERATED);
        // Add other intents here...
        LocalBroadcastManager.getInstance(this).registerReceiver(myBroadCastReceiver, intentFilter);
        Log.d(TAG, "reciever registered");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(getApplicationContext()).unregisterReceiver(myBroadCastReceiver);
        // TODO: Empty the queue?
    }

    class MyBroadCastReceiver extends BroadcastReceiver
    {
        @Override
        public void onReceive(Context context, Intent intent) {
            try
            {
                Log.d(TAG, "onReceive() called");

                while (!workQueue.isEmpty()) {
                    String action = workQueue.get();
                    if (IntentActions.LOOKUP_USER.equals(action)) {
                        lookup_user();
                    } else {
                        Log.e(TAG, "Intent received has no valid action");
                    }
                }
            }
            catch (Exception ex)
            {
                ex.printStackTrace();
            }

        }
    }
}
