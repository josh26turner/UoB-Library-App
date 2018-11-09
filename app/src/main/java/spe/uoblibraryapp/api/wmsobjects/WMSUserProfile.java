package spe.uoblibraryapp.api.wmsobjects;

import org.w3c.dom.Node;

import java.util.List;

import spe.uoblibraryapp.api.ncip.WMSNCIPElement;

public class WMSUserProfile {

    List<WMSLoan> loans;
    List<WMSRequest> on_hold;
    List<WMSRequest> recently_recieved;


    public WMSUserProfile(WMSNCIPElement elemHolder) {
        // TODO: Run some checks on elem to ensure it is correct and then extract the data.
        Node node = elemHolder.getElem();





    }

}
