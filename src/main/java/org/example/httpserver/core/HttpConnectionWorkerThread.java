package org.example.httpserver.core;

import org.example.http.*;
import org.example.httpserver.core.io.ReadFileException;
import org.example.httpserver.core.io.WebRootHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class HttpConnectionWorkerThread extends Thread {

    private static final Logger LOGGER = LoggerFactory.getLogger(ServerListenThread.class);
    private final Socket socket;
    private WebRootHandler wRH;
    private HttpParser parser = new HttpParser();

    public HttpConnectionWorkerThread(Socket socket, WebRootHandler wRH) {
        this.socket = socket;
        this.wRH = wRH;
    }

    @Override
    public void run() {

        InputStream iS = null;
        OutputStream oS = null;

      try {
         iS = socket.getInputStream();
         oS = socket.getOutputStream();

         /* do reading
         String html =
                 "<html>" +
                         "<head>" +
                         "<title>" +
                         "Raw Dawg" +
                         "</title>" +
                         "</head>" +
                         "</body>" +
                         "<h1>" +
                         "HTTP Server in JAVA with Jackson!" +
                         "</h1>" +
                         "</body>" +
                         "</html>";

         final String CRLF = "\n\r";

         String res = "HTTP/1.1 200 OK" + CRLF + //status code - http version res_code res_message
                 "Content-Length: " + html.getBytes().length + CRLF //header
                 + CRLF +
                 html +
                 CRLF + CRLF;

         oS.write(res.getBytes());

         //close

         //serverSocket.close();
         try {
             sleep(1000);
         } catch (InterruptedException e) {
             e.printStackTrace();
         }*/

          HttpRequest req = parser.parserHttpRequest(iS);
          HttpResponse res = handleRequest(req);

          oS.write(res.getResBytes());

         LOGGER.info(" Connection Processing finished......");
      } catch (IOException e) {
         LOGGER.error("Problem with communication");
      } catch (HttpParsingException | BadHttpVersionException e) {
          throw new RuntimeException(e);
      } finally {
         if(iS != null) {
             try {
                 iS.close();
             } catch (IOException ignored) {}
         }

         if(oS != null) {
             try {
                 oS.close();
             } catch (IOException ignored) {}
         }

         if(socket != null) {
             try {
                 socket.close();
             } catch (IOException ignored) {}
         }
      }
    }

    private HttpResponse handleRequest(HttpRequest req) {

        HttpMethod method = req.getMethod();

        if(method == null) {
            return new HttpResponse.Builder()
                    .httpVersion(req.getCompatibleVersion().LITERAL)
                    .statusCode(HttpStatusCode.SERVER_ERROR_501_NOT_IMPLEMENTED)
                    .build();
        }

        return switch (req.getMethod()) {
            case GET -> {
                LOGGER.info(" * GET Request");
                yield handleGetRequest(req, true);
            }
            case HEAD -> {
                LOGGER.info(" * HEAD Request");
                yield handleGetRequest(req, false);
            }
            default -> new HttpResponse.Builder()
                    .httpVersion(req.getCompatibleVersion().LITERAL)
                    .statusCode(HttpStatusCode.SERVER_ERROR_501_NOT_IMPLEMENTED)
                    .build();
        };
    }

    private HttpResponse handleGetRequest(HttpRequest req, boolean setMessageBody) {
        try {
            HttpResponse.Builder builder = new HttpResponse.Builder()
                    .httpVersion(req.getCompatibleVersion().LITERAL)
                    .statusCode(HttpStatusCode.OK)
                    .addHeader(HttpHeaderName.CONTENT_TYPE.headerName, wRH.getFileMimeType(req.getReqTarget()));

            if(setMessageBody) {
                byte[] messageBody = wRH.getFileByteArrayData(req.getReqTarget());
                builder.addHeader(HttpHeaderName.CONTENT_LENGTH.headerName, String.valueOf(messageBody.length))
                        .messageBody(messageBody);
            }

            return builder.build();

        } catch (FileNotFoundException e) {

            return new HttpResponse.Builder()
                    .httpVersion(req.getCompatibleVersion().LITERAL)
                    .statusCode(HttpStatusCode.CLIENT_ERROR_404_NOT_FOUND)
                    .build();

        } catch (ReadFileException e) {

            return new HttpResponse.Builder()
                    .httpVersion(req.getCompatibleVersion().LITERAL)
                    .statusCode(HttpStatusCode.SERVER_ERROR_500_INTERNAL_SERVER_ERROR)
                    .build();
        }
    }
    
}
