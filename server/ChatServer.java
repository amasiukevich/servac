package server;

import network.TCPConnection;
import network.TCPConnectionObserver;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.net.InetAddress;
import java.util.logging.Logger;

/**
 * Can't be controlled from outside
 * */

public class ChatServer implements TCPConnectionObserver {

    /**
     * a utility method for testing purposes
     */
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

    /**
     * List of all connections
     */
    private final ArrayList<TCPConnection> connections = new ArrayList<>();
    //  private MessageLogger dataLogger;


    /**
     * A private constructor, since we want to create only one instance of our server
     */
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

    /**
     * Adds a new connection to the list of connections and informs all the users about it
     * @param connection a connection instance we use
     */
    @Override
    public synchronized void onConnectionReady(TCPConnection connection) {
        connections.add(connection);
        sendToAllConnections("Client " + connection + " connected");
    }

    /**
     * Informs all the users of the chat (someone connected) etc....
     * @param connection a connection instance we use
     * @param value a message value that server received
     */
    @Override
    public synchronized void onReceiveString(TCPConnection connection, String value) {
        sendToAllConnections(value);
    }

    /**
     * Removes the disconnected user from the list and informs all the users in the chat
     * @param connection - a connection instance we use
     */

    @Override
    public synchronized void onDisconnect(TCPConnection connection) {
        connections.remove(connection);
        sendToAllConnections("Client " + connection + " disconnected.");
    }


    /**
     * Performes when exception occured
     * @param connection a connection instance we use
     * @param exception an exception occured
     * */
    @Override
    public synchronized void onException(TCPConnection connection, Exception exception) {
        System.out.println("TCPConnection exception: " + exception);
    }

    /**
     * Method that logs into console the value and sends string to the connections
     * @param value a string to be sent
     */
    private void sendToAllConnections(String value) {
        final int size = connections.size();
        for (int i = 0; i < size; i++)
            connections.get(i).sendString(value);
    }
}
