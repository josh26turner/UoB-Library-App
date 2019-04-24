package spe.uoblibraryapp.api.ncip;

import android.content.Context;

import com.android.volley.toolbox.StringRequest;

public abstract class WMSNCIPAPIRequest {

    String requestBody;
    String url;
    private boolean paramsAdded;

    StringRequest createRequest(Context context) throws ParamsNotSetException{
        if (!paramsAdded) throw new ParamsNotSetException();
        return null;
    }

    void setRequestBodyParams(String ... ps){
        for (String p : ps){
            requestBody = String.format(requestBody, p);
        }
        paramsAdded = true;
    }
}
