package ClientStuff;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {

    private static final int PORT = 21599;
    private static final String HOST = "localhost";

    public Client() {
        try {
            Socket socket = new Socket(HOST, PORT);
            InputStream in = socket.getInputStream();
            OutputStream out = socket.getOutputStream();
        } catch (IOException e) {
            System.out.println("Caught an exception");
        }
        
    }
}
