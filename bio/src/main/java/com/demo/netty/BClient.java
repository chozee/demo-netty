package com.demo.netty;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

public class BClient {
    private static final int PORT = 8081;
    private static final String HOST = "127.0.0.1";


    public static void main(String[] args) {
        try {
            start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void start() throws Exception {
        InetAddress address = InetAddress.getLocalHost();
        InetSocketAddress addressSocket = InetSocketAddress.createUnresolved(HOST, PORT);

        Socket client = new Socket(address, PORT);
        Socket client2 = new Socket();
        client.connect(addressSocket);
        client.getOutputStream().write("HelloServer".getBytes());
        client.getOutputStream().flush();
        client.getOutputStream().close();
        client.close();
    }

}
