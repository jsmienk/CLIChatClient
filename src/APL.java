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

    private Socket socket;

    private User user;

    public static void main(String[] args) {
        // run the server
        new APL().run();
    }

    private void run() {
        while (true) {
            try {
                connect();
                break;
            } catch (IOException ioe) {
//            ioe.printStackTrace();
                System.err.print("Could not reach the server at this time.\nTrying again in 10 seconds.");

                // sleep for 5 seconds
                try {
                    for (int i = 0; i < 10; i++) {
                        System.out.print(".");
                        Thread.sleep(1000);
                    }
                    System.out.println();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        // start two threads
        new MessageListener(socket).start();
        new MessageSender(socket, user).start();
    }

    private void connect() throws IOException {
        // create the connection
        socket = new Socket(APL.SERVER_ADDRESS, APL.SERVER_PORT);

        final Scanner scanner = new Scanner(System.in);
        // ask for username
        System.out.println("Please, enter your username:");
        final String username = scanner.next();

        // ask for a valid colour
        String colour = "";
            System.out.println("Please, enter your colour:");
            colour = scanner.next();


        // confirm
        System.out.println("You have chosen username: " + username);
        System.out.println("You have chosen colour: " + colour);

        user = new User(username, colour);
    }
}
