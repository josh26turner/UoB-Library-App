package spe.uoblibraryapp.api;


import org.junit.Test;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import static junit.framework.TestCase.fail;
import static junit.framework.Assert.assertEquals;


/**
 * Tests to check the XMLResponse
 */

public class XMLResponseTest {

    private Document getXMLDocument(String xml){
        XMLResponse parser = new XMLResponse();
        Document doc = null;
        try {
            doc = parser.parseXML(xml);
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
    public void parseSomeReallyReallyBasicXml(){
        String xml = "<?xml version=\"1.0\"?> <test attribute=\"hello\">123</test>";
        Document doc = getXMLDocument(xml);
    }

    @Test
    public void getRootElementTagTest(){
        Document doc = getXMLDocument(
                "<?xml version=\"1.0\"?> <test attribute=\"hello world\">123</test>");
        Element root = doc.getDocumentElement();
        assertEquals(
                "The tag name does not match",
                "test",
                root.getTagName());
    }

    @Test
    public void getRootElementAttributeTest(){
        Document doc = getXMLDocument(
                "<?xml version=\"1.0\"?> <test attribute=\"hello world\">123</test>");
        Element root = doc.getDocumentElement();
        assertEquals(
                "The attribute does not match",
                "hello world",
                root.getAttribute("attribute"));
    }

    @Test
    public void getRootElementContentsTest(){
        Document doc = getXMLDocument(
                "<?xml version=\"1.0\"?> <test attribute=\"hello world\">123</test>");
        Element root = doc.getDocumentElement();
        assertEquals(
                "The contents does not match",
                "123",
                root.getTextContent());

    }



}
