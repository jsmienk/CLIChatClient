import json.JSONArray;
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
                        System.err.println("The ChatServer has stopped! See you again later.");
                        APL.stop();
                        return;
                    }

                    try {
                        // receive the message as json
                        JSONObject serverJSON = new JSONObject(serverData);

                        // an error message
                        if (serverJSON.has("error")) {
                            final String error = serverJSON.optString("error", "");
                            final String serverName = serverJSON.optString("server_name", "SERVER");

                            switch (error) {
                                case "username-exists":
                                    System.err.println("[" + serverName + "]: Username is already in use.");
                                    APL.mutex.release();
                                    break;
                                case "whisper-to-noone":
                                    System.err.println("[" + serverName + "]: No one is using that username.");
                                    break;
                                default:
                                    break;
                            }
                            continue;
                        }

                        // accept message
                        if (serverJSON.has("accept") && serverJSON.optString("accept").equals("accept")) {
                            APL.userAccepted = true;
                            APL.mutex.release();
                        }

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
                                ColorOut.println("** " + from + " whispers to you: " + whisper + " **", ColorOut.Colour.getColour(colour));
                            continue;
                        }

                        // an action message
                        if (serverJSON.has("me")) {
                            final String me = serverJSON.optString("me", "");
                            final String colour = serverJSON.optString("colour", "");

                            if (!me.isEmpty())
                                ColorOut.println(me, ColorOut.Colour.getColour(colour));
                        }

                        // a user list message
                        if (serverJSON.has("userlist")) {
                            final JSONArray list = serverJSON.optJSONArray("userlist");

                            if (list.length() > 0) {
                                String message = ColorOut.Colour.BLACK.getColourCode() + "List of users: \n";

                                for (int i = 0; i < list.length(); i++) {
                                    final JSONObject user = list.optJSONObject(i);
                                    final String username = user.optString("username", "");
                                    final String colour = user.optString("colour", "BLACK");
                                    message += ColorOut.Colour.getColour(colour).getColourCode() + username + "\n";
                                }

                                message += ColorOut.RESET;

                                System.out.println(message);
                            }
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
