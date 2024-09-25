package org.example.httpserver;

import org.example.httpserver.config.Config;
import org.example.httpserver.config.ConfigManager;
import org.example.httpserver.core.ServerListenThread;
import org.example.httpserver.core.io.WebRootNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class Main {

    private final static Logger LOGGER = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) throws IOException {

        LOGGER.info("Server starting... ");

        ConfigManager.getInstance().loadConfigFile("src/main/resources/http.json");
        Config config = ConfigManager.getInstance().getCurrentConfig();

        LOGGER.info("Using Port: {}", config.getPort());
        LOGGER.info("Webroot: {}", config.getWebroot());

        //now do the socket programming in java
        try {
            ServerListenThread serverListenThread = new ServerListenThread(config.getPort(), config.getWebroot());
            serverListenThread.start();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (WebRootNotFoundException e) {
            LOGGER.error("Webroot folder not found", e);
        }
    }
}