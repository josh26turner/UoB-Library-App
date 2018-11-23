package spe.uoblibraryapp.api.ncip;

import org.w3c.dom.Document;

import spe.uoblibraryapp.api.WMSResponse;
import spe.uoblibraryapp.api.XMLParser;

public class WMSNCIPResponse  extends XMLParser implements WMSResponse {
    private String xml;
    WMSNCIPResponse(String xml){
        super(xml);
        this.xml = xml;
    }

    public String rawResponse() {
        return this.xml;
    }

    public Boolean didFail() {
        // TODO: Add check here to parse xml and test if the request failed. Requires knowledge of failed response to complete this
        try {
            Document parsedResponse = this.parse();
        } catch (Exception e){
            return true; // Make this do something.
        }
        return null;
    }
}
