package spe.uoblibraryapp.api.ncip;

import spe.uoblibraryapp.api.WMSResponse;

public class ConcreteWMSNCIPStaffService implements WMSNCIPStaffService {
    @Override
    public WMSResponse checkOut(String userId, String itemId) {
        return null;
    }

    @Override
    public WMSResponse checkIn(String itemId) {
        return null;
    }

    @Override
    public WMSResponse requestItem() {
        return null;
    }

    @Override
    public WMSResponse requestBibliographic() {
        return null;
    }

    @Override
    public WMSResponse cancelRequest() {
        return null;
    }

    @Override
    public WMSResponse cancelBibliographic() {
        return null;
    }
}
