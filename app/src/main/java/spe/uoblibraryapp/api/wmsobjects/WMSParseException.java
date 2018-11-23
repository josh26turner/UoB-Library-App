package spe.uoblibraryapp.api.wmsobjects;

public class WMSParseException extends Exception {
    private String message;

    /**
     * Can initialise the exception with a message
     * @param message
     */
    public WMSParseException(String message){
        this.message = message;
    }

    public WMSParseException(){
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
