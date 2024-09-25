package org.example.httpcore;

import org.example.httpserver.core.io.ReadFileException;
import org.example.httpserver.core.io.WebRootHandler;
import org.example.httpserver.core.io.WebRootNotFoundException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.io.FileNotFoundException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class WebRootHandlerTest {

    private WebRootHandler wRH;

    private Method checkIfEndWithSlash;

    private Method checkIfRelativeExists;

    @BeforeAll
    public void beforeClass() throws WebRootNotFoundException, NoSuchMethodException {
        wRH = new WebRootHandler("src/test/resources");
        Class<WebRootHandler> cls = WebRootHandler.class;
        checkIfEndWithSlash = cls.getDeclaredMethod("checkEndsWithSlash", String.class);
        checkIfEndWithSlash.setAccessible(true);

        checkIfRelativeExists = cls.getDeclaredMethod("checkRelativeExists", String.class);
        checkIfRelativeExists.setAccessible(true);
    }

    @Test
    void constructorGoodPath() {
        try {
            WebRootHandler wRH = new WebRootHandler("C:\\Users\\abhij\\OneDrive\\Desktop\\HttpServerV2\\WebRoot");

        } catch (WebRootNotFoundException e) {
            fail(e);
        }
    }

    @Test
    void constructorBadPath() {
        try {
            WebRootHandler wRH = new WebRootHandler("C:\\Users\\abhij\\OneDrive\\Desktop\\HttpServerV2\\WebRoot");

        } catch (WebRootNotFoundException e) {}
    }

    @Test
    void constructorGoodPath2() {
        try {
            WebRootHandler wRH = new WebRootHandler("WebRoot");

        } catch (WebRootNotFoundException e) {
            fail(e);
        }
    }

    @Test
    void constructorBadPath2() {
        try {
            WebRootHandler wRH = new WebRootHandler("WebRoot");

        } catch (WebRootNotFoundException e) {}
    }

    @Test
    void checkIfEndWithSlash() {
        try {
            boolean result = (boolean) checkIfEndWithSlash.invoke(wRH, "index.html");
            assert result;
        } catch (IllegalAccessException | InvocationTargetException e) {
            fail(e);
        }
    }

    @Test
    void checkIfEndWithSlash2() {
        try {
            boolean result = (boolean) checkIfEndWithSlash.invoke(wRH, "/index.html");
            assert result;
        } catch (IllegalAccessException | InvocationTargetException e) {
            fail(e);
        }
    }

    @Test
    void checkIfEndWithSlash3() {
        try {
            boolean result = (boolean) checkIfEndWithSlash.invoke(wRH, "/private/index.html");
            assert result;
        } catch (IllegalAccessException | InvocationTargetException e) {
            fail(e);
        }
    }

    @Test
    void checkIfEndsWithSlashMethodTrue() {
        try {
            boolean result = (Boolean) checkIfEndWithSlash.invoke(wRH,"/");
            assertTrue(result);
        } catch (IllegalAccessException | InvocationTargetException e) {
            fail(e);
        }
    }

    @Test
    void checkIfEndsWithSlashMethodTrue2() {
        try {
            boolean result = (Boolean) checkIfEndWithSlash.invoke(wRH,"/private/");
            assertTrue(result);
        } catch (IllegalAccessException | InvocationTargetException e) {
            fail(e);
        }
    }

    @Test
    void testWebRootFilePathExists() {
        try {
            boolean result = (boolean) checkIfRelativeExists.invoke(wRH, "/index.html");
            assertTrue(result);
        } catch (IllegalAccessException | InvocationTargetException e) {
            fail(e);
        }
    }

    @Test
    void testWebRootFilePathExistsGoodRelative() {
        try {
            boolean result = (boolean) checkIfRelativeExists.invoke(wRH, "/./././index.html");
            assertTrue(result);
        } catch (IllegalAccessException | InvocationTargetException e) {
            fail(e);
        }
    }

    @Test
    void testWebRootFilePathExistsDoesNotExist() {
        try {
            boolean result = (boolean) checkIfRelativeExists.invoke(wRH, "/indexNotHere.html");
            assertFalse(result);
        } catch (IllegalAccessException | InvocationTargetException e) {
            fail(e);
        }
    }

    @Test
    void testWebRootFilePathExistsInvalid() {
        try {
            boolean result = (boolean) checkIfRelativeExists.invoke(wRH, "/../LICENSE");
            assertFalse(result);
        } catch (IllegalAccessException | InvocationTargetException e) {
            fail(e);
        }
    }

    @Test
    void testGetFileMimeTypeText() {
        try {
            String mimeType = wRH.getFileMimeType("/");
            assertEquals("text/html", mimeType);
        } catch (FileNotFoundException e) {
            fail(e);
        }
    }

    @Test
    void testGetFileMimeTypePng() {
        try {
            String mimeType = wRH.getFileMimeType("/logo.png");
            assertEquals("image/png", mimeType);
        } catch (FileNotFoundException e) {
            fail(e);
        }
    }

    @Test
    void testGetFileMimeTypeDefault() {
        try {
            String mimeType = wRH.getFileMimeType("/favicon.ico");
            assertEquals("application/octet-stream", mimeType);
        } catch (FileNotFoundException e) {
            fail(e);
        }
    }

    @Test
    void testGetFileByteArrayData() {
        try {
            assertTrue(wRH.getFileByteArrayData("/").length > 0);
        } catch (FileNotFoundException | ReadFileException e) {
            fail(e);
        }
    }

    @Test
    void testGetFileByteArrayDataFileNotThere() {
        try {
            wRH.getFileByteArrayData("/test.html");
            fail();
        } catch (FileNotFoundException e) {
            // pass
        } catch (ReadFileException e) {
            fail(e);
        }
    }
}
