package spe.uoblibraryapp.api.ncip;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

import spe.uoblibraryapp.api.WMSException;
import spe.uoblibraryapp.api.WMSResponse;
import spe.uoblibraryapp.api.wmsobjects.WMSUserProfile;

/**
 * Controls the OCLC WMS NCIP API, will hide all XML from users outside of this package.
 */
public class WMSNCIPController {
    /**
     * Used to control the WMS NCIP API
     */
    // TODO: Add methods for each api request type
    // All methods in this class will return wms objects, no XML.

    private WMSNCIPPatronService patronService;

    public WMSNCIPController(Boolean mock){
        // Initialise patron service
        WMSNCIPPatronServiceFactory patronServiceFactory = new WMSNCIPPatronServiceFactory();
        patronServiceFactory.setMock(mock);
        this.patronService = patronServiceFactory.getService();

        // Initialise staff service
        // TODO: Create this when jerry has made it.

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
     * @param user_id
     * @return
     * @throws WMSException
     */
    public WMSUserProfile get_user_details(String user_id) throws WMSException{
        WMSResponse response = this.patronService.lookup_user(user_id);
        if (response.did_fail()){
            throw new WMSException();
        }

        Document doc = response.parse_response();
        Node node = doc.getElementsByTagName("LookupUserResponse").item(0);
        return new WMSUserProfile(new WMSNCIPElement(node));
    }




}
