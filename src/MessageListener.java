import java.io.*;
import java.net.Socket;

/**
 * Author: Jeroen
 * Date created: 17-11-16
 */
class MessageListener extends Thread {

    private InputStream is;
    private OutputStream os;

    MessageListener() throws IOException {
        System.out.println("Trying to connect to: " + APL.SERVER_ADDRESS + ":" + APL.SERVER_PORT);

        Socket socket = new Socket(APL.SERVER_ADDRESS, APL.SERVER_PORT);
        if (socket.isConnected()) System.out.println("Successfully connected.. Happy chatting!");

        while (socket.isConnected()) {
            is = socket.getInputStream();
            os = socket.getOutputStream();

            PrintWriter writer = new PrintWriter(os);
            writer.println("Hallo!");
            writer.flush();
        }
    }

    @Override
    public void run() {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            String line = reader.readLine();

            System.out.println("[CONSOLE]: " + line);
        } catch (IOException ioe) {
            ioe.printStackTrace();
            System.err.println("BufferedReader IOException occurred.");
        }
    }
}
