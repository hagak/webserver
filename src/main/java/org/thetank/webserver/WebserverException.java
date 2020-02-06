package org.thetank.webserver;

public class WebserverException extends Exception {

    private static final long serialVersionUID = 1L;

    private String httpErrorMessage = "Unknown HTTP execption";

    /**
     * For HTTP exceptions
     * @param message Error message to return for http exception
     */
    public WebserverException(String message) {
        super(message);
        this.httpErrorMessage = message;
    }
    
    /**
     * Returns the HTTP Status message to put in the header of the 
     * @return HTTP Error Status Message
     */
    public String getHttpErrorMessage(){
        return this.httpErrorMessage;
    }
}
