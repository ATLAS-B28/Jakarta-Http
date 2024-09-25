package org.example.http;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class HttpHeaderParserTest {

    private HttpParser httpParser;
    private Method parseHeaderMethod;

    @BeforeAll
    public void beforeClass() throws NoSuchMethodException {
        httpParser = new HttpParser();
        Class<HttpParser> cls = HttpParser.class;
        parseHeaderMethod = cls.getDeclaredMethod("parserHeaders", InputStreamReader.class, HttpRequest.class);
        parseHeaderMethod.setAccessible(true);
    }

    @Test
    public void testSimpleSingleHeader() throws InvocationTargetException, IllegalAccessException {
        HttpRequest req = new HttpRequest();
        parseHeaderMethod.invoke(
                httpParser,
                generateSimpleSingleHeaderMessage(),
                req
        );
        assertEquals(1, req.getHeaderNames().size());
        assertEquals("localhost:8080", req.getHeader("host"));
    }

    @Test
    public void testMultiHeaders() throws InvocationTargetException, IllegalAccessException {
        HttpRequest req = new HttpRequest();
        parseHeaderMethod.invoke(
                httpParser,
                generateMultiHeadersMessage(),
                req
        );
        assertEquals(10, req.getHeaderNames().size());
        assertEquals("localhost:8080", req.getHeader("host"));
    }

    @Test
    public void testErrorSpaceBeforeColonHeader() throws IllegalAccessException {
        HttpRequest req = new HttpRequest();

        try {
            parseHeaderMethod.invoke(
                    httpParser,
                    generateSpaceBeforeColonHeaderMessage(),
                    req
            );
        } catch (InvocationTargetException e) {
            if(e.getCause() instanceof HttpParsingException) {
                assertEquals(HttpStatusCode.CLIENT_ERROR_400_BAD_REQUEST, ((HttpParsingException)e.getCause()).getErrorCode());
            }
        }
    }

    private InputStreamReader generateSimpleSingleHeaderMessage() {
        String rawData = "Host: localhost:8080\r\n";

        InputStream iS = new ByteArrayInputStream(
                rawData.getBytes(
                        StandardCharsets.US_ASCII
                )
        );

        InputStreamReader iR = new InputStreamReader(iS, StandardCharsets.US_ASCII);
        return iR;
    }

    private InputStreamReader generateMultiHeadersMessage() {
        String rawData = """
                Host: localhost:8080\r
                Connection: keep-alive\r
                Upgrade-Insecure-Requests: 1\r
                User-Agent: Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.97 Safari/537.36\r
                Sec-Fetch-User: ?1\r
                Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3\r
                Sec-Fetch-Site: none\r
                Sec-Fetch-Mode: navigate\r
                Accept-Encoding: gzip, deflate, br\r
                Accept-Language: en-US,en;q=0.9,es;q=0.8,pt;q=0.7,de-DE;q=0.6,de;q=0.5,la;q=0.4\r
                \r
                """;
        InputStream inputStream = new ByteArrayInputStream(
                rawData.getBytes(
                        StandardCharsets.US_ASCII
                )
        );

        return new InputStreamReader(inputStream, StandardCharsets.US_ASCII);

    }

    private InputStreamReader generateSpaceBeforeColonHeaderMessage() {
        String rawData = "Host : localhost:8080\r\n\r\n";

        InputStream iS = new ByteArrayInputStream(
                rawData.getBytes(
                        StandardCharsets.US_ASCII
                )
        );

        return new InputStreamReader(iS, StandardCharsets.US_ASCII);
    }


}
