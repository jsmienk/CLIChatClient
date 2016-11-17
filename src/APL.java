import java.io.*;
import java.net.Socket;

/**
 * Author: Jeroen
 * Date created: 17-11-16
 */
public class APL {

    static final String SERVER_ADDRESS = "localhost";
    static final int SERVER_PORT = 1234;

    public static void main(String[] args) {
        new APL().run();
    }

    private void run() {
        try {
            // create the connection
            final Socket socket = new Socket(APL.SERVER_ADDRESS, APL.SERVER_PORT);

            // start two threads
            new MessageListener(socket).start();
            new MessageSender(socket).start();
        } catch (IOException ioe) {
            ioe.printStackTrace();
            System.err.println("Socket IOException occurred.");
        }
    }
}
