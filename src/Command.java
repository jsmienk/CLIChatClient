import java.io.IOException;
import java.net.Socket;

/**
 * Author: Jeroen
 * Date created: 18-11-16
 */
enum Command {

    quit,
    help,
    me,
    whisper;

    static void execute(String commandLine) throws IOException {
        final String[] arguments = commandLine.toLowerCase().substring(1).split(" ");
        final String command = arguments[0];

        // if only a '/' is entered
        if (arguments.length == 0) return;

        // quit
        if (command.equals(quit.toString())) {
            System.out.println("Bye!");
            APL.stop();
            return;
        }

        // help
        if (command.equals(help.toString())) {
            printCommands();
            return;
        }

        // me
        if (command.equals(me.toString()) && arguments.length > 1) {
            String msg = "";
            for (int i = 1; i < arguments.length; i++) {
                msg += arguments[i] + " ";
            }

            APL.msgS.sendMe(msg);
            return;
        }

        // whisper
        if (command.equals(whisper.toString()) && arguments.length > 2) {
            String msg = "";
            for (int i = 2; i < arguments.length; i++) {
                msg += arguments[i] + " ";
            }

            APL.msgS.sendWhisper(arguments[1], msg);
            return;
        }

        System.err.println("'/" + command + "' is not a valid command. Type /help for all valid commands.");
    }

    private static void printCommands() {
        int count = 0;
        for (Command c : Command.values()) {
            count++;

            System.out.print(c.getCommandHowto() + "\t");

            if (count == 4) System.out.println();
        }
    }

    private String getCommandHowto() {
        switch (this) {
            case quit:
                return "/quit";
            case help:
                return "/help";
            case me:
                return "/me <action>";
            case whisper:
                return "/whisper <username> <message>";
            default:
                return "";
        }
    }
}
