import java.io.*;
import java.net.Socket;
import java.util.Scanner;

/**
 * Author: Jeroen
 * Date created: 17-11-16
 */
public class APL {

    static final String SERVER_ADDRESS = "localhost";
    static final int SERVER_PORT = 1234;

    private User user;

    public static void main(String[] args) {
        // run the server
        new APL().run();
    }

    private void run() {
        try {
            // create the connection
            final Socket socket = new Socket(APL.SERVER_ADDRESS, APL.SERVER_PORT);

            final Scanner scanner = new Scanner(System.in);
            // ask for username
            System.out.println("Please, enter your username:");
            final String username = scanner.next();

            // ask for a valid colour
            String colour = "";
            while (colour.length() != 7 || colour.charAt(0) != '#') {
                System.out.println("Please, enter your colour as #RRGGBB:");
                colour = scanner.next();
            }

            // confirm
            System.out.println("You have chosen username: " + username);
            System.out.println("You have chosen colour: " + colour);

            user = new User(username, colour);

            // start two threads
            new MessageListener(socket).start();
            new MessageSender(socket, user).start();
        } catch (IOException ioe) {
            ioe.printStackTrace();
            System.err.println("Socket IOException occurred.");
        }
    }
}
