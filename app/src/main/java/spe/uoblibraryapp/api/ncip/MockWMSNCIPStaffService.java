package spe.uoblibraryapp.api.ncip;

import spe.uoblibraryapp.api.WMSResponse;

class MockWMSNCIPPatronService implements WMSNCIPPatronService {
    @Override
    public check_out(String user_id, String item_id){
        return null;
    }

    @Override
    public check_in(String item_id){
        return null;
    }
    
    @Override
    public request_item(){
        return null;
    }

    @Override request_bibliographic(){
        return null;
    }

    @Override
    public cancel_request(){
        return null;
    }

    @Override
    public cancel_bibliographic(){
        return null;
    }

    private WMSResponse create_response(String xml){
        return new WMSNCIPResponse(xml);
    }
}