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

                while (!socket.isClosed() && socket.isConnected()) {
                    System.out.println(".");

                    try {
                        JSONObject json = new JSONObject(reader.readLine());
                        final String username = json.optString("username", "default");
                        final String colour = json.optString("colour", "#000000");
                        final String message = json.optString("message", "");

                        if (!message.isEmpty()) System.out.println("[" + username + "]: " + message);
                    } catch (JSONException je) {
//                        je.printStackTrace();
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
