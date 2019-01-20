package spe.uoblibraryapp.api.ncip;

import spe.uoblibraryapp.api.AuthenticationNeededException;
import spe.uoblibraryapp.api.WMSException;
import spe.uoblibraryapp.api.WMSResponse;
import spe.uoblibraryapp.api.wmsobjects.WMSParseException;

public interface WMSNCIPPatronService {
    void lookup_user() throws AuthenticationNeededException, WMSException, WMSParseException;
    WMSResponse renew_item(String book_id);
    WMSResponse renew_all();
    WMSResponse request_item();
    WMSResponse request_bibliographic();
    WMSResponse update_request();
    WMSResponse cancel_request();
}
