import java.awt.*;
import java.io.*;
import java.net.Socket;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * Author: Jeroen
 * Date created: 17-11-16
 */
public class APL {

    static final String SERVER_ADDRESS = "localhost";
    static final int SERVER_PORT = 1234;

    private Socket socket;

    private User user;

    public static void main(String[] args) {
        // run the server
        new APL().run();
    }

    private void run() {
        // try to connect
        while (true) {
            try {
                connect();
                break;
            } catch (IOException ioe) {
                System.err.print("Could not reach the server at this time.\nTrying again in 10 seconds.");

                // sleep for 5 seconds
                try {
                    for (int i = 0; i < 10; i++) {
                        System.err.print(".");
                        Thread.sleep(1000);
                    }
                    System.err.println("\n");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        // start two threads
        new MessageListener(socket).start();
        new MessageSender(socket, user).start();
    }

    /**
     * Try to connect and register the user
     *
     * @throws IOException if connecting goes wrong
     */
    private void connect() throws IOException {
        // create the connection
        socket = new Socket(APL.SERVER_ADDRESS, APL.SERVER_PORT);

        final Scanner scanner = new Scanner(System.in);
        // ask for username
        System.out.println("Please, enter your username:");
        final String username = scanner.next();

        // ask for a valid colour
        System.out.println("\nPlease, enter your colour:");
        ColorOut.Colour.printColours();
        ColorOut.Colour colour = ColorOut.Colour.getColour(scanner.next());

        // done!
        System.out.print("Welcome, ");
        ColorOut.println(username + "!\n", colour);

        user = new User(username, colour);
    }
}
