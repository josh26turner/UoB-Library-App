package spe.uoblibraryapp.api.collection;

import spe.uoblibraryapp.api.WMSResponse;

public class MockWMSCollectionManagementService implements WMSCollectionManagementService {

    /**
     * Will read the resource details for the itemId
     * @param itemId this is the itemId of the book
     * @return returns the response
     */
    @Override
    public WMSResponse readResource(String itemId) {

        return null;
    }
}
