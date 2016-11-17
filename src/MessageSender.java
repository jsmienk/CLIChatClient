import json.JSONObject;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

/**
 * Author: Jeroen
 * Date created: 17-11-16
 */
class MessageSender extends Thread {


    private final Socket socket;

    private final User user;

    MessageSender(Socket socket, User user) {
        this.socket = socket;
        this.user = user;
    }

    @Override
    public void run() {
        try {
            OutputStream os = socket.getOutputStream();
            PrintWriter writer = new PrintWriter(os);

            // send the first message with a username and colour
            JSONObject jsonUser = new JSONObject();
            jsonUser.put("username", user.getUsername());
            jsonUser.put("colour", user.getColour());

            writer.println(jsonUser.toString());
            writer.flush();

            final Scanner scanner = new Scanner(System.in);
            //noinspection InfiniteLoopStatement
            while (true) {
                if (scanner.hasNextLine()) {
                    final String message = "{ message: "+scanner.nextLine().trim()+"}";

                    // command
                    if (message.charAt(0) == '/') {
                        switch (executeCommand(message)) {
                            case 0:
                                // quit
                                return;
                            default:
                        }
                        continue;
                    }

                    writer.println(message);
                    writer.flush();
                }
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
            System.err.println("Your message could not be send!");
        }
    }

    /**
     * Try to execute a command the user entered
     * @param command /abc
     */
    private int executeCommand(final String command) throws IOException {
        switch (command) {
            case "/quit":
                socket.close();
                return 0;
            default:
                // TODO: 17-11-16 RED COLOR
                System.out.println("'" + command + "' is not a valid command.");
                return 1;
        }
    }
}
