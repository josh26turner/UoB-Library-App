package spe.uoblibraryapp.api.ncip;

public class WMSNCIPPatronServiceFactory {

    private Boolean mock = false;

    public WMSNCIPPatronService getService(){
        // TODO: Return either mock or concrete Service.
        // For now just always return mock.
        return new MockWMSNCIPPatronService();
    }

    public void setMock(Boolean enable){
        this.mock = enable;
    }
}
