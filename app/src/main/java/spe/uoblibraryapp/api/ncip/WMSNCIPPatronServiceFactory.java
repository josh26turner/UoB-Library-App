package spe.uoblibraryapp.api.ncip;

public class WMSNCIPPatronServiceFactory {

    private Boolean mock = false;

    public WMSNCIPPatronService getService(){
        // For now just always return mock.
        if (mock){
            return new MockWMSNCIPPatronService();
        } else{
            // Should return ConcreteWMSNCIPPatronService once we have access to
            // the api and it is implemented.
            return new ConcreteWMSNCIPPatronService();
        }
    }

    public void setMock(Boolean enable){
        this.mock = enable;
    }
}
