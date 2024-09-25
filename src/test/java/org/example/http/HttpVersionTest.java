package org.example.http;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.fail;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class HttpVersionTest {

    @Test
    void getCompatibleVersionExactMatch() {
        HttpVersion version = null;
        try {
            version = HttpVersion.getHttp11("HTTP/1.1");
        } catch (BadHttpVersionException e) {
            fail();
        }
        assertNotNull(version);
        assertEquals(version, HttpVersion.HTTP_1_1);
    }

    @Test
    void getBestCompatibleVersionBadFormats() {
        HttpVersion version = null;
        try {
            version = HttpVersion.getHttp11("http/1.1");
        } catch (BadHttpVersionException e) {

        }
    }

    @Test
    void getBestCompatibleVersionHigherVersion() {
        HttpVersion version = null;
        try {
            version = HttpVersion.getHttp11("HTTP/1.2");
            assertNotNull(version);
            assertEquals(version, HttpVersion.HTTP_1_1);
        } catch (BadHttpVersionException e) {
            fail();
        }
    }



























}
