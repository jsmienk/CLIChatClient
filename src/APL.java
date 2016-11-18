import java.io.*;
import java.net.Socket;
import java.util.Scanner;

/**
 * Author: Jeroen
 * Date created: 17-11-16
 */
public class APL {

    private static final String SERVER_ADDRESS = "localhost";

    private static final int SERVER_PORT = 1234;

    static MessageSender msgS;

    private static Socket socket;

    private User user;

    private APL() {

    }

    public static void main(String[] args) {
        // run the server
        new APL().run();
    }

    static void stop() throws IOException {
        socket.close();
        msgS.exit();
        System.exit(0);
    }

    private void run() {
        // try to connect
        while (true) {
            try {
                System.out.println("Trying to connect to: " + APL.SERVER_ADDRESS + ":" + APL.SERVER_PORT);
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

        // start a thread that listens for messages
        new MessageListener(socket).start();

        try {
            // start a thread that can send messages
            msgS = new MessageSender(socket, user);
            msgS.start();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
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
        System.out.print("\nWelcome, ");
        ColorOut.println(username + "!", colour);
        System.out.println("Just type any messages from now on and press ENTER.\nOr type '/help' to see avaiable commands.\n");

        user = new User(username, colour);
    }
}
