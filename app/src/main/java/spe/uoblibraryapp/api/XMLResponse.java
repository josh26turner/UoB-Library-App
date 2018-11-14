package spe.uoblibraryapp.api;

import org.w3c.dom.*;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.*;
import java.io.*;

public class XMLResponse {

    private String xml;

    public XMLResponse(String xml){
        this.xml = xml;
    }


    /**
     * Parses the XML response from an API request.
     * https://www.tutorialspoint.com/java_xml/java_dom_parse_document.htm
     */
    public Document parse() throws ParserConfigurationException, SAXException, IOException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        InputSource is = new InputSource(new StringReader(xml));
        return builder.parse(is);
    }
}
