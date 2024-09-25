package org.example.http;

public class HttpResponse extends HttpMessage {

    private final String CRLF = "\r\n";

    private String httpVersion;

    private HttpStatusCode statusCode;

    private String reasonPhrase = null;

    public HttpResponse() {}

    public String getHttpVersion() {
        return httpVersion;
    }

    public void setHttpVersion(String httpVersion) {
        this.httpVersion = httpVersion;
    }

    public HttpStatusCode getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(HttpStatusCode statusCode) {
        this.statusCode = statusCode;
    }

    public String getReasonPhrase() {
        if(reasonPhrase == null && statusCode != null) {
            return statusCode.MESSAGE;
        }
        return reasonPhrase;
    }

    public void setReasonPhrase(String reasonPhrase) {
        this.reasonPhrase = reasonPhrase;
    }

    public byte[] getResBytes() {
        StringBuilder resBuilder = new StringBuilder();
        resBuilder.append(httpVersion)
                .append(" ")
                .append(statusCode.STATUS_CODE)
                .append(" ")
                .append(getReasonPhrase())
                .append(CRLF);

        for(String headerName : getHeaderNames()) {
            resBuilder.append(headerName)
                    .append(": ")
                    .append(getHeader(headerName))
                    .append(CRLF);
        }

        resBuilder.append(CRLF);

        byte[] resBytes = resBuilder.toString().getBytes();

        if(getMessageBody().length == 0) {
            return resBytes;
        }

        byte[] resWithBody = new byte[resBytes.length + getMessageBody().length];
        System.arraycopy(resBytes, 0, resWithBody, 0,resBytes.length);
        System.arraycopy(getMessageBody(), 0, resWithBody, resBytes.length, getMessageBody().length);

        return resWithBody;
    }

    public static class Builder {
        private HttpResponse res = new HttpResponse();

        public Builder httpVersion(String httpVersion) {
            res.setHttpVersion(httpVersion);
            return this;
        }

        public Builder statusCode(HttpStatusCode statusCode) {
            res.setStatusCode(statusCode);
            return this;
        }

        public Builder reasonPhrase(String reasonPhrase) {
            res.setReasonPhrase(reasonPhrase);
            return this;
        }

        public Builder messageBody(byte[] messageBody) {
            res.setMessageBody(messageBody);
            return this;
        }

        public HttpResponse build() {
            return res;
        }

        public Builder addHeader(String headerName, String headerField) {
            res.addHeader(headerName, headerField);
            return this;
        }
    }
}
