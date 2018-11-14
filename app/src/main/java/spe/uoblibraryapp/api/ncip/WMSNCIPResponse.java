package spe.uoblibraryapp.api.ncip;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import spe.uoblibraryapp.api.WMSResponse;
import spe.uoblibraryapp.api.XMLResponse;

public class WMSNCIPResponse  extends XMLResponse implements WMSResponse {
    private String xml;

    WMSNCIPResponse(String xml){
        super(xml);
    }

    public String rawResponse() {
        return this.xml;
    }

//    public Document parseResponse() {
//        try {
//            return parseXML(this.xml);
//        } catch (Exception e){
//            return null;
//        }
//    }

    public Boolean didFail() {
        // TODO: Add check here to parse xml and test if the request failed.
        try {
            Document parsedResponse = this.parse();
        } catch (Exception e){
            return null; // Make this do something.
        }
        // TODO: Do some magical stuff with parsed_response
        return null;
    }

    @Override
    public Document parse() throws ParserConfigurationException, SAXException, IOException {
        Document document = super.parse();
        //Do some NCIP specific stuff here
        return document;
    }
}
