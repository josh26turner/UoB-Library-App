package spe.uoblibraryapp.api;



import org.junit.jupiter.api.Test;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;


/**
 * Tests to check the XMLParser
 */

public class XMLParserTest {

    private Document getXMLDocument(String xml){
        XMLParser parser = new XMLParser(xml);
        Document doc = null;
        try {
            doc = parser.parse();
        } catch (ParserConfigurationException e) {
            fail("There was a issue configuring the parser");
        } catch (SAXException e) {
            fail("The XML failed to parse");
        } catch (IOException e) {
            fail("Now an IOException");
        }
        return doc;
    }

    @Test
    void parseSomeReallyReallyBasicXml(){
        String xml = "<?xml version=\"1.0\"?> <test attribute=\"hello\">123</test>";
        Document doc = getXMLDocument(xml);
    }

    @Test
    void getRootElementTagTest(){
        Document doc = getXMLDocument(
                "<?xml version=\"1.0\"?> <test attribute=\"hello world\">123</test>");
        Element root = doc.getDocumentElement();
        assertEquals(
                "test",
                root.getTagName(),
                "The tag name does not match"
        );
    }

    @Test
    void getRootElementAttributeTest(){
        Document doc = getXMLDocument(
                "<?xml version=\"1.0\"?> <test attribute=\"hello world\">123</test>");
        Element root = doc.getDocumentElement();
        assertEquals(
                "hello world",
                root.getAttribute("attribute"),
                "The attribute does not match"
        );
    }

    @Test
    void getRootElementContentsTest(){
        Document doc = getXMLDocument(
                "<?xml version=\"1.0\"?> <test attribute=\"hello world\">123</test>");
        Element root = doc.getDocumentElement();
        assertEquals(
                "123",
                root.getTextContent(),
                "The contents does not match"
        );

    }



}
