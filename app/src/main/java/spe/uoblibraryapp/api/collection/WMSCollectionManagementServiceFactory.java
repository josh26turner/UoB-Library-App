package spe.uoblibraryapp.api.collection;


public class WMSCollectionManagementServiceFactory {
    private Boolean mock = false;

    public WMSCollectionManagementService getService(){
        // For now just always return mock.
        if (mock){
            return new MockWMSCollectionManagementService();
        } else{
            // Should return ConcreteWMSCollectionManagementService once we have access to
            // the api and it is implemented.
            return new MockWMSCollectionManagementService();
        }
    }

    public void setMock(Boolean enable){
        this.mock = enable;
    }

}
