import java.io.IOException;
import java.net.Socket;

/**
 * Author: Jeroen
 * Date created: 18-11-16
 */
enum Command {

    quit,
    help;

    static void execute(final Socket socket, String command) throws IOException {
        command = command.toLowerCase().substring(1);

        // quit
        if (command.equals(quit.toString())) {
            socket.close();
            return;
        }

        // help
        if (command.equals(help.toString())) {
            printCommands();
            return;
        }

        System.err.println("'/" + command + "' is not a valid command. Type /help for all valid commands.");
    }

    private static void printCommands() {
        int count = 0;
        for (Command c : Command.values()) {
            count++;

            System.out.print("/" + c);

            if (count == 4) System.out.println();
        }
    }
}
