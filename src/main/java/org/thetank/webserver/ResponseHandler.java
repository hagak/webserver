package org.thetank.webserver;

import java.io.PrintWriter;
import java.util.Date;

public class ResponseHandler {
    
    private String statusMessage;
    private String payload;
    private PrintWriter responseOut;

    /**
     * This really should be a fleshed out class but for the exercise it seemed a bit excessive
     * @param responseOut
     * @param statusMessage
     * @param payload
     */
    public ResponseHandler(PrintWriter responseOut, String statusMessage, String payload) {
        this.statusMessage = statusMessage;
        this.payload = payload;
        this.responseOut = responseOut;

        responseOut.println("HTTP/1.1 " + statusMessage);
        responseOut.println("Date: " + new Date());
        responseOut.println("Content-type: application/json");
        responseOut.println("Content-length: " + payload.length());
        responseOut.println(); //http spec requires two blank lines between header and content
        responseOut.println(payload);//Content returned
        responseOut.flush(); // flush character output stream buffer
    }

    public void sendResponse(){
        this.responseOut.println("HTTP/1.1 " + this.statusMessage);
        this.responseOut.println("Date: " + new Date());
        this.responseOut.println("Content-type: application/json");
        this.responseOut.println("Content-length: " + this.payload.length());
        this.responseOut.println(); //http spec requires two blank lines between header and content
        this.responseOut.println(this.payload);//Content returned
        responseOut.flush(); // flush character output stream buffer
    }
}
