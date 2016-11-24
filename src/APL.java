import json.JSONObject;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.Semaphore;

/**
 * Author: Jeroen
 * Date created: 17-11-16
 */
public class APL {

    private static final String SERVER_ADDRESS = "192.168.1.102";//"163.158.182.205";

    private static final int SERVER_PORT = 25565;

    static MessageSender msgS;

    static boolean userAccepted;

    static Semaphore mutex;

    private static Socket socket;

    private User user;

    private APL() {
        userAccepted = false;
        mutex = new Semaphore(0);
    }

    public static void main(String[] args) {
        // run the server
        new APL().run();
    }

    static void stop() throws IOException {
        if (socket != null) socket.close();
        if (msgS != null) msgS.exit();
        System.exit(0);
    }

    private void run() {
        // try to connect
        while (true) {
            try {
                connect();

                // start a thread that listens for messages
                new MessageListener(socket).start();

                signin();

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

        try {
            // start a thread that can send messages
            msgS = new MessageSender(socket, user);
            msgS.start();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    /**
     * Try to connect the client
     *
     * @throws IOException if connecting goes wrong
     */
    private void connect() throws IOException {
        System.out.println("Trying to connect to: " + APL.SERVER_ADDRESS + ":" + APL.SERVER_PORT);
        // create the connection
        socket = new Socket(APL.SERVER_ADDRESS, APL.SERVER_PORT);
    }

    /**
     * Try to sign in the user
     *
     * @throws IOException if signing in goes wrong
     */
    private void signin() throws IOException {
        final Scanner scanner = new Scanner(System.in);

        String username = "";
        ColorOut.Colour colour = ColorOut.Colour.BLACK;

        while (!userAccepted) {
            // ask for username
            System.out.println("Please, enter your username:");
            username = scanner.next();

            // ask for a valid colour
            System.out.println("\nPlease, enter your colour:");
            ColorOut.Colour.printColours();
            colour = ColorOut.Colour.getColour(scanner.next());

            // send the first message with a username and colour
            JSONObject jsonUser = new JSONObject();
            jsonUser.put("username", username);
            jsonUser.put("colour", colour);

            final PrintWriter writer = new PrintWriter(socket.getOutputStream());
            writer.println(jsonUser.toString());
            writer.flush();

            try {
                mutex.acquire();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // done!
        System.out.print("\nWelcome, ");
        ColorOut.print(username, colour);
        System.out.println("!\nJust type any messages from now on and press ENTER.\n" +
                "Or type '/help' to see avaiable commands.\n");

        user = new User(username, colour);
    }
}
