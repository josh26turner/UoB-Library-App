package spe.uoblibraryapp.api.wmsobjects;

import org.w3c.dom.Node;

import spe.uoblibraryapp.api.ncip.WMSNCIPElement;
import spe.uoblibraryapp.api.ncip.WMSNCIPPatronService;
import spe.uoblibraryapp.api.ncip.WMSNCIPStaffService;

public class WMSBook {

    private WMSNCIPPatronService patronService;
    private WMSNCIPStaffService staffService;

    public WMSBook(WMSNCIPElement elemHolder,
                   WMSNCIPPatronService patronService,
                   WMSNCIPStaffService staffService
    ) throws WMSParseException{

        // Save Services
        this.patronService = patronService;
        this.staffService = staffService;

        // TODO: Run some checks on elem to ensure it is correct and then extract the data.
        Node element = elemHolder.getElem();
    }
}
