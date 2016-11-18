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

    private final Scanner scanner;

    private final PrintWriter writer;

    MessageSender(Socket socket, User user) throws IOException {
        this.socket = socket;
        this.user = user;
        scanner = new Scanner(System.in);
        writer = new PrintWriter(socket.getOutputStream());
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

            //noinspection InfiniteLoopStatement
            while (true) {
                if (scanner.hasNextLine()) {
                    final String message = scanner.nextLine().trim();

                    if (message.length() > 0) {
                        // command
                        if (message.charAt(0) == '/' && message.length() > 1) {
                            Command.execute(message);
                            continue;
                        }

                        final JSONObject json = new JSONObject();
                        json.put("message", message);

                        writer.println(json.toString());
                        writer.flush();
                    }
                }
            }

        } catch (IOException ioe) {
            ioe.printStackTrace();
            System.err.println("Your message could not be send!");
        }
    }

    void exit() throws IOException {
        scanner.close();
    }

    /**
     * Send a message in the form of a action e.g. *Jeroen is working*
     *
     * @param action what the user is doing
     */
    void sendMe(final String action) {
        // send the first message with a username and colour
        JSONObject json = new JSONObject();
        json.put("me", action);

        writer.println(json.toString());
        writer.flush();
    }

    /**
     * Send a message only to a certain user
     */
    void sendWhisper(final String username, final String message) {
        // send the first message with a username and colour
        JSONObject json = new JSONObject();
        json.put("to", username);
        json.put("whisper", message);

        writer.println(json.toString());
        writer.flush();
    }
}
