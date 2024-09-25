package org.example.http;

public class HttpRequest extends HttpMessage {

    private HttpMethod method;
    private String reqTarget;
    private String originalHttpVersion;
    private HttpVersion compatibleVersion;

    HttpRequest() {}

    public HttpMethod getMethod() {
        return method;
    }

    public String getReqTarget() {
        return reqTarget;
    }

    public HttpVersion getCompatibleVersion() {
        return compatibleVersion;
    }

    public String getOriginalHttpVersion() {
        return originalHttpVersion;
    }

    void setMethod(String methodName) throws HttpParsingException {
        //for a method from the enum and see if name is same
        //as the method's name from the incoming stream
        //set this and return
        for(HttpMethod method : HttpMethod.values()) {
            if(methodName.equals(method.name())) {
                this.method = method;
                return;
            }
        }

        throw new HttpParsingException(
                HttpStatusCode.SERVER_ERROR_501_NOT_IMPLEMENTED
        );
    }

    void setReqTarget(String reqTarget) throws HttpParsingException {
        if(reqTarget == null || reqTarget.isEmpty()) {
            throw new HttpParsingException(HttpStatusCode.SERVER_ERROR_500_INTERNAL_SERVER_ERROR);
        }

        this.reqTarget = reqTarget;
    }

    void setOriginalHttpVersion(String originalHttpVersion) throws BadHttpVersionException, HttpParsingException {
        this.originalHttpVersion = originalHttpVersion;
        this.compatibleVersion = HttpVersion.getHttp11(originalHttpVersion);

        if(this.compatibleVersion == null) {
            throw new HttpParsingException(
                    HttpStatusCode.SERVER_ERROR_505_HTTP_VERSION_NOT_SUPPORTED
            );
        }
    }
}
