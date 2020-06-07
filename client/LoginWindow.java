package client;

import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author Anton Masiukevich
 * @version 1.0
 * @since 2020-05-29
 *
 * A class that represents a login page.
 * The user must enter the name to join the chat
 *
 */

public class LoginWindow extends JFrame implements ActionListener, ComponentListener {

    private static int n_windows = 0;

    /**
     * A welcoming label
     * */
    private JLabel welcomeLabel = new JLabel("Welcome to JChat");

    /**
     * A label to enter the IP Address of a server you want to connect to
     */
    private JLabel ipAddrLabel = new JLabel("Enter an IP address: ");

    /**
     * A text field to enter the IP Address of a server you want to connect to
     * */
    private JTextField ipAddrField = new JTextField("192.168.1.201");

    /**
     * A label to enter the port of a server you want to connect to
     */
    private JLabel portLabel = new JLabel("Enter a port number: ");

    /**
     * A text field to enter the port of a server you want to connect to
     * */
    private JTextField portField = new JTextField("8180");

    /**
     * A label for a nickname
     * */
    private JLabel label = new JLabel("Enter a nickname: ");

    /**
     * A text field to enter a nickname
     */
    private JTextField nicknameField = new JTextField();

    /**
     * A button which starts the chat
     * */
    private JButton button = new JButton("Start Messaging");

    /**
     * Constructor to a login page.
     * Defines the layout
     */
    public LoginWindow() {

        setSize(800, 600);

        this.n_windows += 1;


        welcomeLabel.setLayout(null);
        welcomeLabel.setFont(new Font("Verdana", Font.PLAIN, 50));
        welcomeLabel.setSize(150, 80);


        label.setLayout(null);
        label.setFont(new Font("Verdana", Font.PLAIN, 32));
        label.setSize(100, 40);

        ipAddrLabel.setLayout(null);
        ipAddrLabel.setFont(new Font("Verdana", Font.PLAIN, 32));
        label.setSize(100, 40);

        portLabel.setLayout(null);
        portLabel.setFont(new Font("Verdana", Font.PLAIN, 32));
        portLabel.setSize(100, 40);


        nicknameField.setSize(100, 50);

        JPanel panel = new JPanel();
        panel.setBorder(new EmptyBorder(100, 40, 100, 40));
        panel.setLayout(new GridLayout(5, 2, 10, 12));
        panel.add(welcomeLabel);
        panel.add(new JLabel());
        panel.add(ipAddrLabel);
        ipAddrField.addActionListener(this);
        panel.add(ipAddrField);

        panel.add(portLabel);
        portField.addActionListener(this);
        panel.add(portField);

        panel.add(label);
        nicknameField.addActionListener(this);
        panel.add(nicknameField);

        button.addActionListener(this);
        panel.add(button);

        add(panel);
        pack();

        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);

        setLocationRelativeTo(null);
        setVisible(true);
    }

    /**
     * A method to listen on possible left mouse or "Enter" button clicks.
     * If the click is performed, method creates a new client with a given name
     * @param e
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        // TODO: specify warning when nickname is empty
        String nickname = nicknameField.getText();
        String ipAddress = ipAddrField.getText();
        int port;
        try {
            port = Integer.parseInt(portField.getText());
            if (!isValidIP(ipAddress)) {
                JOptionPane.showMessageDialog(this, "Invalid IP address");
                nicknameField.setText("");
                ipAddrField.setText("");
                portField.setText("");
                this.setVisible(true);
                return;
            }
        } catch (Exception exception) {
            System.out.println(exception);
            JOptionPane.showMessageDialog(this, "Port supposed to be an integer");
            nicknameField.setText("");
            ipAddrField.setText("");
            portField.setText("");
            this.setVisible(true);
            return;
        }

        // System.out.println(nickname);
//
        try {
            nicknameField.setText("");
            ipAddrField.setText("");
            portField.setText("");
            setVisible(false);
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    new Client(nickname, ipAddress, port);
                }
            });
        } catch (Exception exception) {
            System.out.println("An exception occured: " + e);
        }
    }

    private boolean isValidIP(String ipCandidate) {
        String IPV4_REGEX = "^(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\." +
                "(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\." +
                "(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\." +
                "(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$";

        Pattern IPV4_PATTERN = Pattern.compile(IPV4_REGEX);

        Matcher matcher = IPV4_PATTERN.matcher(ipCandidate);

        return matcher.matches();

    }

    @Override
    public void componentResized(ComponentEvent e) {}

    @Override
    public void componentMoved(ComponentEvent e) {}

    @Override
    public void componentShown(ComponentEvent e) {}

    @Override
    public void componentHidden(ComponentEvent e) {
        if (this.n_windows == 1) setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        else {
            this.n_windows -= 1;
        }

    }
}
