package org.example.http;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HttpParser {

    private static final int SP = 0x20;
    private static final int CR = 0x0D;
    private static final int LF = 0x0A;
    private final static Logger LOGGER = LoggerFactory.getLogger(HttpParser.class);

    public HttpRequest parserHttpRequest(InputStream iS) throws HttpParsingException, BadHttpVersionException {
        InputStreamReader iR = new InputStreamReader(iS, StandardCharsets.US_ASCII);

        HttpRequest req = new HttpRequest();

        try {
            parserRequestLine(iR, req);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            parserHeaders(iR, req);
        } catch (IOException e) {
            e.printStackTrace();
        }

        parseBody(iR, req);

        return req;
    }

    private void parserRequestLine(InputStreamReader iR, HttpRequest req) throws IOException, HttpParsingException {
           StringBuilder processingBuffer = new StringBuilder();

           boolean methodParse = false;
           boolean reqTargetParse = false;

           int _byte;

           while((_byte = iR.read()) >= 0) {
               if(_byte == CR) {
                   _byte = iR.read();
                   if(_byte == LF) {

                       LOGGER.debug("Request line VERSION to process : {}", processingBuffer);
                       if(!methodParse || !reqTargetParse) {
                           throw new HttpParsingException(HttpStatusCode.CLIENT_ERROR_400_BAD_REQUEST);
                       }

                       try {
                           req.setOriginalHttpVersion(processingBuffer.toString());
                       } catch (BadHttpVersionException e) {
                           throw new HttpParsingException(HttpStatusCode.SERVER_ERROR_501_NOT_IMPLEMENTED);
                       }

                       return;

                   } else {
                       throw new HttpParsingException(HttpStatusCode.CLIENT_ERROR_400_BAD_REQUEST);
                   }
               }

               if(_byte == SP) {
                   if(!methodParse) {
                       LOGGER.debug("Request line METHOD to process : {}", processingBuffer);
                       req.setMethod(processingBuffer.toString());
                       methodParse = true;
                   } else if(!reqTargetParse) {
                       LOGGER.debug("Request line Target REQUEST to process : {}", processingBuffer);
                       req.setReqTarget(processingBuffer.toString());
                       reqTargetParse = true;
                   } else {
                       throw new HttpParsingException(HttpStatusCode.CLIENT_ERROR_400_BAD_REQUEST);
                   }
                   processingBuffer.delete(0, processingBuffer.length());
               } else {
                   processingBuffer.append((char) _byte);
                   if(!methodParse) {
                       if(processingBuffer.length() > HttpMethod.MAX_LENGTH) {
                           throw new HttpParsingException(HttpStatusCode.SERVER_ERROR_501_NOT_IMPLEMENTED);
                       }
                   }
               }
           }
    }

    private void parserHeaders(InputStreamReader iR, HttpRequest req) throws IOException, HttpParsingException {

        StringBuilder processBuffer = new StringBuilder();

        boolean crlfFound = false;

        int _byte;

        while((_byte = iR.read()) >= 0) {
            if(_byte == CR) {
                _byte = iR.read();
                if(_byte == LF) {
                    if(!crlfFound) {
                        crlfFound = true;
                        
                        processSingleHeadersField(processBuffer, req);

                        processBuffer.delete(0, processBuffer.length());
                    } else {//2 crlf received end of headers section
                        return;
                    }
                } else {
                    throw new HttpParsingException(HttpStatusCode.CLIENT_ERROR_400_BAD_REQUEST);
                }
            } else {
                
              crlfFound = false;
              processBuffer.append((char) _byte);
            }
        }
    }

    private void processSingleHeadersField(StringBuilder processBuffer, HttpRequest req) throws HttpParsingException {
        String rawHeaderField = processBuffer.toString();
        Pattern pattern = Pattern.compile("^(?<fieldName>[!#$%&’*+\\-./^_‘|˜\\dA-Za-z]+):\\s?(?<fieldValue>[!#$%&’*+\\-./^_‘|˜(),:;<=>?@[\\\\]{}\" \\dA-Za-z]+)\\s?$");

        Matcher matcher = pattern.matcher(rawHeaderField);

        if(matcher.matches()) {
            String fieldName = matcher.group("fieldName");
            String fieldValue = matcher.group("fieldValue");
            req.addHeader(fieldName, fieldValue);
        } else {
            throw new HttpParsingException(HttpStatusCode.CLIENT_ERROR_400_BAD_REQUEST);
        }
    }


    private void parseBody(InputStreamReader iR, HttpRequest req) {}

}























