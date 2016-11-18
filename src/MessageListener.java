import json.JSONException;
import json.JSONObject;

import java.io.*;
import java.net.Socket;

/**
 * Author: Jeroen
 * Date created: 17-11-16
 */
class MessageListener extends Thread {

    private final Socket socket;

    MessageListener(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {

        System.out.println("Trying to connect to: " + APL.SERVER_ADDRESS + ":" + APL.SERVER_PORT);

        try {
            // connect to the server
            if (socket.isConnected()) {
                System.out.println("Successfully connected.. Happy chatting!");

                final InputStream is = socket.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));

                // keep receiving messages
                while (!socket.isClosed() && socket.isConnected()) {
                    try {
                        // receive the message as json
                        JSONObject json = new JSONObject(reader.readLine());

                        final String username = json.optString("username", "");
                        final String colour = json.optString("colour", "BLACK");
                        final String message = json.optString("message", "");

                        // if the user was defined and a message was sent
                        if (!message.isEmpty() && !username.isEmpty())
                            ColorOut.println("[" + username + "]: " + message, ColorOut.Colour.getColour(colour));
                    } catch (JSONException je) {

                        je.printStackTrace();
                        System.err.println("Receiving a message went wrong.");
                    }
                }
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
            System.err.println("BufferedReader IOException occurred.");
        }
    }
}
