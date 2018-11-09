package spe.uoblibraryapp.api.wmsobjects;

import org.w3c.dom.Element;

import spe.uoblibraryapp.api.ncip.WMSNCIPElement;

public class WMSRequest {


    public WMSRequest(WMSNCIPElement elemHolder) {
        // TODO: Run some checks on elem to ensure it is correct and then extract the data.
        Element element = elemHolder.getElem();
    }
}
