package org.example.httpserver.core;

import org.example.httpserver.core.io.WebRootHandler;
import org.example.httpserver.core.io.WebRootNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerListenThread extends Thread {

    private static final Logger LOGGER = LoggerFactory.getLogger(ServerListenThread.class);
    private int port;
    private String webroot;
    private ServerSocket serverSocket;
    private WebRootHandler wRH;

    public ServerListenThread(int port, String webroot) throws IOException, WebRootNotFoundException {
        this.port = port;
        this.webroot = webroot;
        this.serverSocket = new ServerSocket(this.port);
        this.wRH = new WebRootHandler(webroot);
    }

    @Override
    public void run() {

        try {

            while(serverSocket.isBound() && !serverSocket.isClosed()) {
               Socket socket = serverSocket.accept();

               LOGGER.info(" * Connection accepted: {}", socket.getInetAddress());

               HttpConnectionWorkerThread wT = new HttpConnectionWorkerThread(socket, wRH);
               wT.start();

            }
        } catch (IOException e) {
            LOGGER.error("Problem with setting socket", e);
        } finally {
            if(serverSocket != null)  {
                try {
                    serverSocket.close();
                } catch (IOException e) {}
            }
        }
    }
}
