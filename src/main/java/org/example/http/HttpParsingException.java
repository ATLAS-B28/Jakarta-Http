package org.example.http;

public class HttpParsingException extends Exception {

    private final HttpStatusCode code;

    public HttpParsingException(HttpStatusCode code) {
        super(code.MESSAGE);
        this.code = code;
    }

    public HttpStatusCode getErrorCode() {
        return code;
    }
}
