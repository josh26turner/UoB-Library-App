package spe.uoblibraryapp.api.ncip;

import spe.uoblibraryapp.api.WMSResponse;

public interface WMSNCIPStaffService {
    WMSResponse checkOut(String userId, String itemId);
    WMSResponse checkIn(String itemId);
    WMSResponse requestItem();
    WMSResponse requestBibliographic();
    WMSResponse cancelRequest();
    WMSResponse cancelBibliographic();
}