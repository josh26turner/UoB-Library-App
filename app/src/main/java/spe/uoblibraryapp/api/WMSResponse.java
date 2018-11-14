package spe.uoblibraryapp.api;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

public interface WMSResponse {
    String rawResponse();
    Boolean didFail();
}
