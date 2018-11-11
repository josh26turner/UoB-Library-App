package spe.uoblibraryapp.api.ncip;

import spe.uoblibraryapp.api.WMSResponse;

class MockWMSNCIPPatronService implements WMSNCIPPatronService {
    @Override
    public WMSResponse check_out(String user_id, String item_id){
        //TODO: Add example response
        return null;
    }

    @Override
    public WMSResponse check_in(String item_id){
        //TODO: Add example response
        return null;
    }
    
    @Override
    public WMSResponse request_item(){
        //TODO: Add example response
        return null;
    }

    @Override WMSResponse request_bibliographic(){
        //TODO: Add example response
        return null;
    }

    @Override
    public WMSResponse cancel_request(){
        //TODO: Add example response
        return null;
    }

    @Override
    public WMSResponse cancel_bibliographic(){
        //TODO: Add example response
        return null;
    }

    private WMSResponse create_response(String xml){
        return new WMSNCIPResponse(xml);
    }
}