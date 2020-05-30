package client;

import network.TCPConnection;
import network.TCPConnectionObserver;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

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

public class Client extends JFrame implements ActionListener, TCPConnectionObserver {

    private static String nickname;

    private static final String IP_ADDR = "192.168.1.201";
    private static final int PORT = 8180;
    private static final int HEIGHT = 600;
    private static final int WIDTH = 800;


    private final JTextArea log = new JTextArea(5, 30);
    private final JTextField input = new JTextField(30);
    private final JLabel user = new JLabel();

    private TCPConnection connection;

    public Client(String nickname) {

        this.nickname = nickname;

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

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        try {
            connection = new TCPConnection(this, IP_ADDR, PORT);
        } catch (IOException e) {
            printMessage("Connection exception: " + e);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String msg = input.getText();
        if (msg.equals("")) return;
        input.setText(null);
        connection.sendString(this.nickname + ": " + msg);
    }

    @Override
    public void onConnectionReady(TCPConnection connection) {
        printMessage("Connection Ready...");
    }

    @Override
    public void onReceiveString(TCPConnection connection, String value) {
        printMessage(value);
    }

    @Override
    public void onDisconnect(TCPConnection connection) {
        printMessage("Connection closed...");
    }

    @Override
    public void onException(TCPConnection connection, Exception exception) {
        printMessage("Connection exception " + exception);
    }

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
}
