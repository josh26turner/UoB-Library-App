package spe.uoblibraryapp.api.ncip;

public class WMSNCIPStaffServiceFactory {

    private Boolean mock = false;

    public WMSNCIPStaffService getService(){
        // For now just always return mock.
        if (mock){
            return new MockWMSNCIPStaffService();
        } else{
            // Should return ConcreteWMSNCIPPatronService once we have access to
            // the api and it is implemented.
            return new MockWMSNCIPStaffService();
        }
    }

    public void setMock(Boolean enable){
        this.mock = enable;
    }
}