package org.example.httpserver.core.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URLConnection;

public class WebRootHandler {

    private File webRoot;

    public WebRootHandler(String webRootPath) throws WebRootNotFoundException {
        webRoot = new File(webRootPath);

        if(!webRoot.exists() || !webRoot.isDirectory()) {
            throw new WebRootNotFoundException("Webroot provided does not exist or is not a folder");
        }
    }

    private boolean checkEndsWithSlash(String relativePath) {
        return relativePath.endsWith("/");
    }

    private boolean checkRelativeExists(String relativePath) {
        File file = new File(webRoot, relativePath);

        if(!file.exists()) {
            return false;
        }

        try {
            if(file.getCanonicalPath().startsWith(webRoot.getCanonicalPath())) {
                return true;
            }
        } catch (IOException e) {
            return false;
        }
        return false;
    }

    public String getFileMimeType(String relativePath) throws FileNotFoundException {
        if(checkEndsWithSlash(relativePath)) {
            relativePath += "index.html";
        }
        if(!checkRelativeExists(relativePath)) {
            throw new FileNotFoundException("File not found: " + relativePath);
        }

        File file = new File(webRoot, relativePath);

        String mimeType = URLConnection.getFileNameMap().getContentTypeFor(file.getName());

        if(mimeType == null) {
            return "application/octet-stream";
        }

        return mimeType;
    }

    public byte[] getFileByteArrayData(String relativePath) throws FileNotFoundException, ReadFileException {
        if(checkEndsWithSlash(relativePath)) {
            relativePath += "index.html";
        }

        if(!checkRelativeExists(relativePath)) {
            throw new FileNotFoundException("File not found: " + relativePath);
        }

        File file = new File(webRoot, relativePath);
        FileInputStream fIS = new FileInputStream(file);
        byte[] fileBytes = new byte[(int)file.length()];

        try {
            fIS.read(fileBytes);
            fIS.close();
        } catch (IOException e) {
            throw new ReadFileException(e);
        }

        return fileBytes;
    }
}
