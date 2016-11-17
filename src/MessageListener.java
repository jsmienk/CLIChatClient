import json.JSONException;
import json.JSONObject;

import java.io.*;
import java.net.Socket;

/**
 * Author: Jeroen
 * Date created: 17-11-16
 */
class MessageListener extends Thread {
    private static final String BLACK = "\u001B[30m";
    private static final String RED = "\u001B[31m";
    private static final String GREEN = "\u001B[32m";
    private static final String YELLOW = "\u001B[33m";
    private static final String BLUE = "\u001B[34m";
    private static final String PURPLE = "\u001B[35m";
    private static final String CYAN = "\u001B[36m";
    public static final String RESET = "\u001B[0m";
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
                if (reader.readLine().startsWith("{")) {
                    while (!socket.isClosed() && socket.isConnected()) {
                        try {
                            JSONObject json = new JSONObject(reader.readLine());
                            final String username = json.optString("username","default");
                            String colour = json.optString("colour","#000000");
                            final String message = json.optString("message","");
                            colour = getColours(colour);
                            if(!message.isEmpty())System.out.println(colour+"[" + username + "]: " + message+RESET);
                        } catch (JSONException je) {

                            je.printStackTrace();
                            System.err.println("Receiving a message went wrong.");
                        }
                    }
                }else{
                    System.out.println("Message not json:"+reader.readLine());
                }
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
            System.err.println("BufferedReader IOException occurred.");
        }
    }

    public String getColours(String colour){
        if(colour.equalsIgnoreCase("RED")){
            return RED;
        }else if(colour.equalsIgnoreCase("BLUE")){
            return BLUE;
        }else if(colour.equalsIgnoreCase("YELLOW")){
            return YELLOW;
        }else if(colour.equalsIgnoreCase("GREEN")){
            return GREEN;
        }else if(colour.equalsIgnoreCase("BLACK")){
            return BLACK;
        }else if(colour.equalsIgnoreCase("CYAN")){
            return CYAN;
        }else if(colour.equalsIgnoreCase("PURPLE")){
            return PURPLE;
        }
        return BLACK;
    }
}
