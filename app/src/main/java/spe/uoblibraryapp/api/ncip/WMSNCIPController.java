package spe.uoblibraryapp.api.ncip;

import android.util.Log;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import spe.uoblibraryapp.api.WMSException;
import spe.uoblibraryapp.api.WMSResponse;
import spe.uoblibraryapp.api.wmsobjects.WMSParseException;
import spe.uoblibraryapp.api.wmsobjects.WMSUserProfile;

/**
 * Controls the OCLC WMS NCIP API, will hide all XML from users outside of this package.
 * @author rileyevans
 */
public class WMSNCIPController {
    /**
     * Used to control the WMS NCIP API
     */
    // All methods in this class will return wms objects, no XML.

    private WMSNCIPPatronService patronService;
    private WMSNCIPStaffService staffService;

    public WMSNCIPController(Boolean mock){
        // Initialise patron service
        WMSNCIPPatronServiceFactory patronServiceFactory = new WMSNCIPPatronServiceFactory();
        patronServiceFactory.setMock(mock);
        this.patronService = patronServiceFactory.getService();

        // Initialise staff service
        WMSNCIPStaffServiceFactory staffServiceFactory = new WMSNCIPStaffServiceFactory();
        staffServiceFactory.setMock(mock);
        this.staffService = staffServiceFactory.getService();
    }
    public WMSNCIPController(){
        /**
         * Added as a catch so in standard use we don't need to specify mock=false
         */
        this(false);
    }


    /**
     * This gets the UserProfile for a given user_id.
     *
     * @param userId the ID relating to the user who is requesting the profile.
     * @return returns a WMSUserProfile which has all actions that a user may need to carry out.
     * @throws WMSException thrown if there is an error getting
     */
    public WMSUserProfile getUserDetails(String userId) throws WMSException, WMSParseException {
        WMSResponse response = this.patronService.lookup_user(userId);
        if (response.didFail()) {
            throw new WMSException("There was an error retrieving the User Profile");
        }
        Document doc;
        try {
            doc = response.parse();
        } catch (IOException | SAXException | ParserConfigurationException e){
            throw new WMSException("There was an error Parsing the WMS response");
        }
        Node node = doc.getElementsByTagName("ns1:LookupUserResponse").item(0);
        return new WMSUserProfile(new WMSNCIPElement(node), this.patronService, this.staffService);
    }
}
