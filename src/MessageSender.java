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

    MessageSender(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            OutputStream os = socket.getOutputStream();

            PrintWriter writer = new PrintWriter(os);

            final Scanner scanner = new Scanner(System.in);
            while(true) {
                if (scanner.hasNextLine()) {
                    writer.println(scanner.nextLine());
                    writer.flush();
                }
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
            System.out.println("Your message could not be send!");
        }
    }
}
