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
        if (socket.isConnected()) {
            System.out.println("Successfully connected.. Happy chatting!");
            is = socket.getInputStream();
            os = socket.getOutputStream();
        }
    }

    @Override
    public void run() {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            String line = reader.readLine();

            System.out.println(line);
        } catch (IOException ioe) {
            ioe.printStackTrace();
            System.err.println("BufferedReader IOException occurred.");
        }
    }
}
