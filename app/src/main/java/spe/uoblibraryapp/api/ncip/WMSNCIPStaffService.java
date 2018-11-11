package spe.uoblibraryapp.api.ncip;

import spe.uoblibraryapp.api.WMSResponse;

interface WMSNCIPStaffService {
    WMSResponse check_out(String user_id, String item_id);
    WMSResponse check_in(String item_id);
    WMSResponse request_item();
    WMSResponse request_bibliographic();
    WMSResponse cancel_request();
    WMSResponse cancel_bibliographic();
}