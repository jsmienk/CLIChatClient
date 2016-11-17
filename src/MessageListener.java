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

                while (socket.isConnected()) {
                    String line = reader.readLine();

                    System.out.println("[CONSOLE]: " + line);
                }
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
            System.err.println("BufferedReader IOException occurred.");
        }
    }
}
