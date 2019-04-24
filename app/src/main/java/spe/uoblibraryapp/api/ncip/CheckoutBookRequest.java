package spe.uoblibraryapp.api.ncip;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.nio.charset.StandardCharsets;

import spe.uoblibraryapp.Constants;

public class CheckoutBookRequest extends WMSNCIPAPIRequest {
    private static final String TAG = "CheckoutBookRequest";

    CheckoutBookRequest(){
        requestBody = Constants.RequestTemplates.checkoutBook;
        url = Constants.APIUrls.checkoutBook;
    }

    @Override
    public StringRequest createRequest(Context context) {
        return new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String xml) {
                Intent intent = new Intent(Constants.IntentActions.CHECKOUT_BOOK_RESPONSE);
                intent.putExtra("xml", xml);
                context.sendBroadcast(intent);
                Log.d(TAG, "broadcast sent");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Error checking out book");
                context.sendBroadcast(new Intent(Constants.IntentActions.CHECKOUT_BOOK_ERROR));
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
    }

}
