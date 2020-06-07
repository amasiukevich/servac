package network;

import java.io.*;
import java.net.Socket;
import java.nio.charset.Charset;

/**
 * A connection class is used in application as a controller.
 * Provides upper-level abstraction upon sockets.
 */

public class TCPConnection {

    private final Socket socket;
    private final Thread recThread;
    private final BufferedReader reader;
    private final BufferedWriter writer;
    private final TCPConnectionObserver eventObserver;


    /**
     * Additional constructor of the connection
     * @param eventObserver - eventObserver that listens for the upcoming events
     * @param ipAddr - ip address of the server we want to connect with
     * @param port - server's port
     * @throws IOException
     */
    public TCPConnection(TCPConnectionObserver eventObserver, String ipAddr, int port) throws IOException {
        this(eventObserver, new Socket(ipAddr, port));
    }

    /**
     * Main constructor of the connection
     * @param eventObserver
     * @param sock  - a socket associated with
     * @throws IOException
     */
    public TCPConnection(TCPConnectionObserver eventObserver, Socket sock) throws IOException {
        this.eventObserver = eventObserver;
        // initialize the socket
        this.socket = sock;
        // initializes reader and writer (from socket - input and output stream)
        reader = new BufferedReader(
                new InputStreamReader(socket.getInputStream(), Charset.forName("UTF-8"))
        );
        writer = new BufferedWriter(
                new OutputStreamWriter(socket.getOutputStream(), Charset.forName("UTF-8"))
        );

        this.recThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    eventObserver.onConnectionReady((TCPConnection.this));

                    while (!recThread.isInterrupted()) {
                        String msg = reader.readLine();
                        eventObserver.onReceiveString(TCPConnection.this, msg);
                    }
                    String msg = reader.readLine();
                } catch (IOException e) {
                    eventObserver.onException(TCPConnection.this, e);
                } finally {
                    eventObserver.onDisconnect(TCPConnection.this);
                }
            }
        });

        recThread.start();
    }

    /**
     * method sends strings through the connection
     * @param value - a string that should be sent
     */

    public synchronized void sendString(String value) {
        try {
            writer.write(value + "\r\n"); // for correct output
            writer.flush();
        } catch (IOException e) {
            eventObserver.onException(this, e);
            disconnect();
        }
    }

    /**
     * A method that interrupts the receiving messages thread of the connection
     */
    public synchronized void disconnect() {
        recThread.interrupt();
        try {
            socket.close();
        } catch (IOException e) {
            eventObserver.onException(this, e);
        }
    }

    /**
     * @return a string representation of the connection
     */
    @Override
    public String toString() {
        return "TCPConnection: " + socket.getInetAddress() + ":" + socket.getPort();
    }
}
