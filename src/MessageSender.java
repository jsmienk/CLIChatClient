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
                    final String message = scanner.nextLine().trim();

                    // command
                    if (message.charAt(0) == '/') {
                        Command.execute(socket, message);
                        continue;
                    }

                    final JSONObject json = new JSONObject();
                    json.put("message", message);

                    writer.println(json.toString());
                    writer.flush();
                }
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
            System.err.println("Your message could not be send!");
        }
    }
}
