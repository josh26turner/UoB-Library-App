package spe.uoblibraryapp.api.ncip;

import spe.uoblibraryapp.api.WMSResponse;

public interface WMSNCIPPatronService {
    WMSResponse lookup_user(String user_id);
    WMSResponse renew_item(String book_id);
    WMSResponse renew_all();
    WMSResponse request_item();
    WMSResponse request_bibliographic();
    WMSResponse update_request();
    WMSResponse cancel_request();
}
