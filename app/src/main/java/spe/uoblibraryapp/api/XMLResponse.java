package spe.uoblibraryapp.api;

import org.w3c.dom.*;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.*;
import java.io.*;

public class XMLResponse {

    /**
     * Parses the XML response from an API request.
     * https://www.tutorialspoint.com/java_xml/java_dom_parse_document.htm
     */
    public Document parseXML(String raw_xml) throws ParserConfigurationException, SAXException, IOException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        InputSource is = new InputSource(new StringReader(raw_xml));
        Document document = builder.parse(is);
        return document;
    }
}
