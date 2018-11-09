package spe.uoblibraryapp.api.wmsobjects;

import org.w3c.dom.Node;

import spe.uoblibraryapp.api.ncip.WMSNCIPElement;

public class WMSBook {

    public WMSBook(WMSNCIPElement elemHolder) {
        // TODO: Run some checks on elem to ensure it is correct and then extract the data.
        Node element = elemHolder.getElem();
    }
}
