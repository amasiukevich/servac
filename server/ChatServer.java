package server;

import network.TCPConnection;
import network.TCPConnectionObserver;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.net.InetAddress;

/**
 * Can't be controlled from outside
 * */

public class ChatServer implements TCPConnectionObserver {

//    public static void main(String[] args) {
//        new ChatServer();
//    }

    private void getLocalIP() {
        InetAddress address = null;
        try {
            address = InetAddress.getLocalHost();
        } catch(UnknownHostException e) {
            System.out.println("Can't get ip address =(");
        }

        System.out.println("IP Address: " + address.getHostAddress());
        System.out.println("Host Name: " + address.getHostName());
    }

    private final ArrayList<TCPConnection> connections = new ArrayList<>();

    // private constructor: only one instance of a server
    public ChatServer() {
        System.out.println("Server running...");
        try(ServerSocket serverSocket = new ServerSocket(8180)) {
            getLocalIP();
            while (true) {
                try {
                    new TCPConnection(this, serverSocket.accept());
                } catch (IOException e) {
                    System.out.println("TCPConnection exception: " + e);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public synchronized void onConnectionReady(TCPConnection connection) {
        connections.add(connection);
        sendToAllConnections("Client connected " + connection);
    }

    @Override
    public synchronized void onReceiveString(TCPConnection connection, String value) {
        sendToAllConnections(value);
    }

    @Override
    public synchronized void onDisconnect(TCPConnection connection) {
        connections.remove(connection);
        sendToAllConnections("Client disconnected: " + connection);
    }

    @Override
    public synchronized void onException(TCPConnection connection, Exception exception) {
        System.out.println("TCPConnection exception: " + exception);
    }

    private void sendToAllConnections(String value) {
        System.out.println(value);
        final int size = connections.size();
        for (int i = 0; i < size; i++)
            connections.get(i).sendString(value);
    }
}
