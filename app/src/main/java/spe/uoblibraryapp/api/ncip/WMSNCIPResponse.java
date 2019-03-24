package spe.uoblibraryapp.api.ncip;

import org.w3c.dom.Document;

import spe.uoblibraryapp.api.WMSResponse;
import spe.uoblibraryapp.api.XMLParser;

public class WMSNCIPResponse  extends XMLParser implements WMSResponse {
    private String xml;
    public WMSNCIPResponse(String xml){
        super(xml);
        this.xml = xml;
    }

    public String rawResponse() {
        return this.xml;
    }

    public Boolean didFail() {
        try {
            Document parsedResponse = this.parse();
        } catch (Exception e){
            return true; // Make this do something.
        }
        return false;
    }
}
