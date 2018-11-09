package spe.uoblibraryapp.api.ncip;

import org.w3c.dom.Document;
import spe.uoblibraryapp.api.WMSResponse;
import spe.uoblibraryapp.api.XMLResponse;

public class WMSNCIPResponse  extends XMLResponse implements WMSResponse {
    private String xml;

    WMSNCIPResponse(String xml){
        this.xml = xml;
    }

    @Override
    public String raw_response() {
        return this.xml;
    }

    public Document parse_response() {
        try {
            return parseXML(this.xml);
        } catch (Exception e){
            return null;
        }
    }

    @Override
    public Boolean did_fail() {
        // TODO: Add check here to parse xml and test if the request failed.
        Document parsed_response = this.parse_response();
        // TODO: Do some magical stuff with parsed_response
        return null;
    }
}
