package org.example.httpserver.config;

public class HttpConfigException extends RuntimeException {
    public HttpConfigException() {
        super();
    }

    public HttpConfigException(String message) {
        super(message);
    }

    public HttpConfigException(String message, Throwable cause) {
        super(message, cause);
    }

    public HttpConfigException(Throwable cause) {
        super(cause);
    }
}
