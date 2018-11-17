package spe.uoblibraryapp.api;

public class WMSException extends Exception {

    private String message;

    /**
     * Can initialise the exception with a message
     * @param message
     */
    public WMSException(String message){
        this.message = message;
    }

    public WMSException(){
        this("");
    }

    /**
     * Will return the message passed in the exception
     * @return
     */
    @Override
    public String getMessage() {
        return message;
    }
}

