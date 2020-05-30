import client.LoginWindow;
import server.ChatServer;

/**
 * <h1>JChat Project</h1>
 *
 * The Main class of chat application that allows multiple users
 * communicate via one server.
 *
 * GUI is written using Java Swing library
 * @author Anton Masiukevich
 * @version 1.0
 * @since 2020-05-29
 */

public class Main {

    public static void main(String[] args) {

        // model

        Thread serverThread = new Thread(new Runnable() {
            @Override
            public void run() {
                ChatServer server = new ChatServer();
            }
        });

        serverThread.start();

        // view
        LoginWindow loginWindow = new LoginWindow();

    }
}
