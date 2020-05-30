package client;

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

public class LoginWindow extends JFrame implements ActionListener {

    private JLabel welcomeLabel = new JLabel("Welcome to JChat");
    private JLabel label = new JLabel("Enter a nickname you want to join the chat with: ");
    private JTextField nicknameInput = new JTextField();
    private JButton button = new JButton("Start Messaging");

    /**
     * Constructor to a login page.
     * Defines the layout
     */

    public LoginWindow() {

        setSize(800, 600);

        // TODO: specify the proper location

        welcomeLabel.setLayout(null);
        welcomeLabel.setFont(new Font("Vivaldi", Font.PLAIN, 50));
        welcomeLabel.setSize(150, 80);


        label.setLayout(null);
        label.setFont(new Font("Verdana", Font.PLAIN, 20));
        label.setSize(250, 80);
//        label.setLocation(0, 0);

        nicknameInput.setSize(100, 50);

        JPanel panel = new JPanel();
        panel.setBorder(new EmptyBorder(100, 40, 100, 40));
        panel.setLayout(new GridLayout(4, 1, 10, 12));
        panel.add(welcomeLabel);
        panel.add(label);

        nicknameInput.addActionListener(this);
        panel.add(nicknameInput);


        button.addActionListener(this);
        panel.add(button);

        add(panel);
        pack();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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
        String nickname = nicknameInput.getText();
        if (!nickname.equals("")) {

            nicknameInput.setText("");
            setVisible(false);
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    new Client(nickname);
                }
            });
        }
    }
}
