package spe.uoblibraryapp.api.wmsobjects;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import spe.uoblibraryapp.api.ncip.WMSNCIPPatronService;
import spe.uoblibraryapp.api.ncip.WMSNCIPPatronServiceFactory;
import spe.uoblibraryapp.api.ncip.WMSNCIPStaffService;
import spe.uoblibraryapp.api.ncip.WMSNCIPStaffServiceFactory;

import static spe.uoblibraryapp.api.XMLParser.parse;


/**
 * This is not a test, it contains some setup functions for WMSUserProfile
 * that help to test it.
 */
public class WMSUserProfileTests{

    /**
     * Creates the user node from an xml response.
     */
    Node createUserNode(String xml) throws ParserConfigurationException, SAXException, IOException {
        Document doc = parse(xml);
        NodeList nodeList = doc.getElementsByTagName("ns1:LookupUserResponse");
        return nodeList.item(0);
    }

    /**
     * Storage class to that allows for both services to be returned.
     */
    class WMSNCIPServices{
        public WMSNCIPStaffService staffService;
        public WMSNCIPPatronService patronService;
        WMSNCIPServices(WMSNCIPPatronService patronService, WMSNCIPStaffService staffService){
            this.patronService = patronService;
            this.staffService = staffService;
        }
    }

    /**
     * Gets the mock services for each NCIP service
     * @return Returns the WMSNCIPServices storage class, allows returning of both services.
     */
    WMSNCIPServices getServices(){
        WMSNCIPPatronServiceFactory patronServiceFactory = new WMSNCIPPatronServiceFactory();
        patronServiceFactory.setMock(true);

        WMSNCIPStaffServiceFactory staffServiceFactory = new WMSNCIPStaffServiceFactory();
        staffServiceFactory.setMock(true);

        return new WMSNCIPServices(
                patronServiceFactory.getService(),
                staffServiceFactory.getService()
        );
    }
}
