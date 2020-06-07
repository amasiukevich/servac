package client;

import network.TCPConnection;
import network.TCPConnectionObserver;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.io.File;
import java.io.IOException;
import java.sql.SQLOutput;
import java.util.ArrayList;

/**
 *
 * @author Anton Masiukevich
 * @version 1.0
 * @since 2020-04-17
 *
 * client.Client class, which implements ActionListener and
 * TCPConnectionObserver interfaces.
 * Each client has an instance of a connection associated with this
 * particular client.
 * The user enters the message into a text field, and when he hits "Enter",
 * The message is passing through a connection to the server.
 */



public class Client extends JFrame implements ActionListener, TCPConnectionObserver, ComponentListener {

    /**
     * A nickname of a user
     * */
    private String nickname;



    // TODO: Make an ability to join a specific server

    /**
     * IP_ADDRESS and PORT
     * */
    private static String IP_ADDR; //"192.168.1.201";
    private static int PORT; //= 8180;

    /**
     * HEIGHT and WIDTH of the main window
     */
    private static final int HEIGHT = 600;
    private static final int WIDTH = 600;


    /**
     * A main area where the text is displayed
     * */
    private final JTextArea log = new JTextArea(5, 30);

    /**
     *
     * */
    private final JTextField input = new JTextField(30);

    /**
     *
     * */
    private final JLabel user = new JLabel();

    /**
     * A connection we're trying to establish
     */
    private TCPConnection connection;

    /**
     * Creates a layout for the client and sets a listener
     * */

    public Client(String nickname, String ipAddr, int port) {

        this.nickname = nickname;
        this.IP_ADDR = ipAddr;
        this.PORT = port;

        setSize(WIDTH, HEIGHT);

        JScrollPane scrollPane = new JScrollPane(log);
        scrollPane.setPreferredSize(new Dimension(360, 420));
        user.setPreferredSize(new Dimension(360, 40));
        input.setPreferredSize(new Dimension(360, 75));

        log.setLineWrap(true);
        log.setWrapStyleWord(true);
        log.setEditable(false);

        user.setText("User: " + this.nickname);


        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();

        gbc.fill = GridBagConstraints.VERTICAL;
        gbc.ipadx = 100;
        gbc.ipady = 0;
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(scrollPane, gbc);

        gbc.fill = GridBagConstraints.VERTICAL;
        gbc.ipadx = 100;
        gbc.gridx = 0;
        gbc.gridy = 1;
        add(user, gbc);

        gbc.fill = GridBagConstraints.VERTICAL;
        gbc.ipadx = 100;
        gbc.gridx = 0;
        gbc.gridy = 2;
        add(input, gbc);

        input.addActionListener(this);

        this.setResizable(false);
        this.setVisible(true);

        setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);

        try {
            connection = new TCPConnection(this, IP_ADDR, PORT);
        } catch (IOException e) {
            printMessage("Connection exception: " + e);
        }
    }

    /**
     * performs action when user presses "Enter" or clicks a mouse
     * @param e
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        String msg = input.getText();
        if (msg.equals("")) return;
        input.setText(null);
        connection.sendString(this.nickname + ": " + msg);
    }


    /**
     * A method handles a connect-of-a-new-user response from the server
     * @param connection a connection we use
     */
    @Override
    public void onConnectionReady(TCPConnection connection) {
        printMessage("Connection Ready...");
    }

    /**
     * A method handles receiving message response from the server
     * */
    @Override
    public void onReceiveString(TCPConnection connection, String value) {
        printMessage(value);
    }
    /**
     * A method handles a disconnect-of-a-user response from the server
     * @param connection - a connection we use
     * */
    @Override
    public void onDisconnect(TCPConnection connection) {
        printMessage("Connection closed...");
    }

    /**
     * Handles exceptions in our connections
     * @param connection
     * @param exception
     */
    @Override
    public void onException(TCPConnection connection, Exception exception) {
        printMessage("Connection exception " + exception);
    }

    /**
     * A utility function that prints the message prints the message
     * @param msg - a message we want to print
     */
    private synchronized void printMessage(String msg) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                log.append(msg + "\n");
                // autoscroll
                log.setCaretPosition(log.getDocument().getLength());
            }
        });
    }

    @Override
    public void componentResized(ComponentEvent e) {}

    @Override
    public void componentMoved(ComponentEvent e) {}

    @Override
    public void componentShown(ComponentEvent e) {}

    @Override
    public void componentHidden(ComponentEvent e) {
        this.connection.disconnect();
    }
}
