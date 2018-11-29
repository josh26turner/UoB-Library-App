package spe.uoblibraryapp.api.collection;

import spe.uoblibraryapp.api.WMSResponse;

public interface WMSCollectionManagementService {
    WMSResponse readResource(String itemId);
}
