package spe.uoblibraryapp.api.ncip;

import spe.uoblibraryapp.api.WMSResponse;

class ConcreteWMSNCIPPartronService implements WMSNCIPPatronService {


    @Override
    public WMSResponse lookup_user(String user_id) {
        return null;
    }

    @Override
    public WMSResponse renew_item(String book_id) { return null; }

    @Override
    public WMSResponse renew_all() {
        return null;
    }

    @Override
    public WMSResponse request_item() {
        return null;
    }

    @Override
    public WMSResponse request_bibliographic() {
        return null;
    }

    @Override
    public WMSResponse update_request() { return null; }

    @Override
    public WMSResponse cancel_request() { return null; }

}
