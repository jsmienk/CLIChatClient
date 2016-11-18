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
        try {
            // connect to the server
            if (socket.isConnected()) {
                final InputStream is = socket.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));

                // keep receiving messages
                while (!socket.isClosed() && socket.isConnected()) {
                    final String serverData = reader.readLine();
                    if (serverData == null) {
                        APL.stop();
                        return;
                    }

                    try {
                        // receive the message as json
                        JSONObject serverJSON = new JSONObject(serverData);

                        // a normal message
                        if (serverJSON.has("username") && serverJSON.has("message")) {
                            final String username = serverJSON.optString("username", "");
                            final String colour = serverJSON.optString("colour", "BLACK");
                            final String message = serverJSON.optString("message", "");

                            // if the user was defined and a message was sent
                            if (!message.isEmpty() && !username.isEmpty())
                                ColorOut.println("[" + username + "]: " + message, ColorOut.Colour.getColour(colour));
                            continue;
                        }

                        // a private message
                        if (serverJSON.has("whisper") && serverJSON.has("from")) {
                            final String whisper = serverJSON.optString("whisper", "");
                            final String from = serverJSON.optString("from", "");
                            final String colour = serverJSON.optString("colour", "");

                            if (!whisper.isEmpty() && !from.isEmpty())
                                ColorOut.println("** " + from + " whisper to you: " + whisper, ColorOut.Colour.getColour(colour));
                            continue;
                        }

                        // an action message
                        if (serverJSON.has("me")) {
                            final String me = serverJSON.optString("me", "");
                            final String colour = serverJSON.optString("colour", "");

                            if (!me.isEmpty())
                                ColorOut.println(me, ColorOut.Colour.getColour(colour));
                        }
                    } catch (JSONException je) {
                        je.printStackTrace();
                        System.err.println("Receiving a message went wrong.");
                    }
                }
            }
        } catch (IOException ignored) {
        }
    }
}
