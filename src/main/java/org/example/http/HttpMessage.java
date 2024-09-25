package org.example.http;

import java.util.HashMap;
import java.util.Set;

public abstract class HttpMessage {

    /*
    * Standard way to get the headers in the form
    * of HashMap in string-string as key-value
    * then a byte array as message-body
    * 2 methods to get header name from headers in a Set
    * of keys. Get the main header by using the header name
    * in a lower case format as key
    * a get and set method for the message-body
    * */
    private final HashMap<String, String> headers = new HashMap<>();
    private byte[] messageBody = new byte[0];

    public Set<String> getHeaderNames() {
        return headers.keySet();
    }

    public String getHeader(String headerName) {
        return headers.get(headerName.toLowerCase());
    }

    public byte[] getMessageBody() {
        return messageBody;
    }

    public void setMessageBody(byte[] messageBody) {
        this.messageBody = messageBody;
    }

    void addHeader(String headerName, String headerField) {
        headers.put(headerName.toLowerCase(), headerField);
    }
}
