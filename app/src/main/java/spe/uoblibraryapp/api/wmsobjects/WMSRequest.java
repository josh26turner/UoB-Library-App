package spe.uoblibraryapp.api.wmsobjects;

import org.w3c.dom.Node;

import spe.uoblibraryapp.api.ncip.WMSNCIPElement;
import spe.uoblibraryapp.api.ncip.WMSNCIPPatronService;
import spe.uoblibraryapp.api.ncip.WMSNCIPStaffService;

public class WMSRequest {

    public WMSRequest(WMSNCIPElement elemHolder) throws WMSParseException{

        // TODO: Run some checks on elem to ensure it is correct and then extract the data.
        Node element = elemHolder.getElem();
    }
}
