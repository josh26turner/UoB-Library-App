package spe.uoblibraryapp.api;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class XMLParser {

    private String xml;

    public XMLParser(String xml){
        this.xml = xml;
    }


    /**
     * Parses the XML response from an API request.
     * https://www.tutorialspoint.com/java_xml/java_dom_parse_document.htm
     */
    public Document parse() throws ParserConfigurationException, SAXException, IOException {
        return parse(this.xml);
    }

    public static Document parse(String xml) throws ParserConfigurationException, SAXException, IOException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        InputSource is = new InputSource(new StringReader(xml));
        return builder.parse(is);
    }
}
