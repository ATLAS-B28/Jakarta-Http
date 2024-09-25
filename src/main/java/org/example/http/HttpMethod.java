package org.example.http;

public enum HttpMethod {

    GET, HEAD;

    public static final int MAX_LENGTH;

    static {
        int tempMaxLen = -1;
        for(HttpMethod method : values()) {
            if(method.name().length() > tempMaxLen) {
                tempMaxLen = method.name().length();
            }
        }
        MAX_LENGTH = tempMaxLen;
    }
}
