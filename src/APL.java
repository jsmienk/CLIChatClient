import java.io.*;

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
            new MessageListener().start();
        } catch (IOException ioe) {
            ioe.printStackTrace();
            System.err.println("Socket IOException occurred.");
        }
    }
}
