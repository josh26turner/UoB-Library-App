package spe.uoblibraryapp.api.collection;

import spe.uoblibraryapp.api.WMSResponse;
import spe.uoblibraryapp.api.wmsobjects.WMSBook;

public class WMSCollectionManagementController {

    private WMSCollectionManagementService service;

    public WMSCollectionManagementController (){
        this(false);
    }

    public WMSCollectionManagementController(Boolean mock){
        WMSCollectionManagementServiceFactory factory = new WMSCollectionManagementServiceFactory();
        factory.setMock(mock);
        service = factory.getService();
    }

    public WMSBook lookupBook(String itemId){
        // TODO:1 Implement mock api
        // TODO:2 Make request using service
        // TODO:3 put response into WMSCollectionManagerElement
        WMSResponse res = service.readResource("test123");
//        return new WMSBook(new WMSCollectionManagementElement());
        if (itemId.equals("151319076")) {
            return new WMSBook("1050042221");
        } else{
            return new WMSBook(new WMSCollectionManagementElement());
        }
    }
}
