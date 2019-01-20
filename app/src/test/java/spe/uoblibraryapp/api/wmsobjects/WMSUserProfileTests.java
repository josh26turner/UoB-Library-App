package spe.uoblibraryapp.api.wmsobjects;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import spe.uoblibraryapp.api.ncip.WMSNCIPElement;

import static org.junit.Assert.fail;
import static spe.uoblibraryapp.api.XMLParser.parse;


/**
 * This is not a test, it contains some setup functions for WMSUserProfile
 * that help to test it.
 */
class WMSUserProfileTests{

    /**
     * Creates the user node from an xml response.
     */
    private Node createUserNode(String xml) throws ParserConfigurationException, SAXException, IOException {
        Document doc = parse(xml);
        NodeList nodeList = doc.getElementsByTagName("ns1:LookupUserResponse");
        return nodeList.item(0);
    }


    WMSUserProfile createUserProfile(String xml){
        Node node = null;
        try {
            node = createUserNode(xml);
        } catch (SAXException | IOException | ParserConfigurationException ex){
            fail(ex.getMessage());
        }

        WMSUserProfile userProfile = null;
        try {
            userProfile = new WMSUserProfile(
                    new WMSNCIPElement(node)
            );
        } catch (WMSParseException ex){
            fail(ex.getMessage());
        }

        return userProfile;
    }
}
