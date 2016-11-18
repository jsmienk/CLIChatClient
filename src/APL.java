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
        int r, g, b;
        System.out.println("Please, enter the RED value for your colour:");
        r = getColorValue(scanner);
        System.out.println("Please, enter the GREEN value for your colour:");
        g = getColorValue(scanner);
        System.out.println("Please, enter the BLUE value for your colour:");
        b = getColorValue(scanner);

        Color colour = new Color(r, g, b);

        // confirm
        System.out.println("\nYou have chosen username: " + username);
        System.out.println("You have chosen colour: " + colour + "\n");

        user = new User(username, colour);
    }

    private int getColorValue(final Scanner sc) {
        int value = -1;
        while (value < 0 || value > 255) {
            try {
                value = sc.nextInt();
            } catch (InputMismatchException ime) {
                System.err.println("Enter a value between 0 and 255");
            }
        }
        return value;
    }
}
