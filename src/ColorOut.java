/**
 * Author: Jeroen
 * Date created: 18-11-16
 */
class ColorOut {

    private static final String RESET = "\u001B[0m";

    static void print(final String string, Colour colour) {
        System.out.print(colour.getColourCode() + string + RESET);
    }

    static void println(final String string, Colour colour) {
        System.out.print(colour.getColourCode() + string + RESET + "\n");
    }

    enum Colour {
        BLACK, RED, GREEN, YELLOW, BLUE, PURPLE, CYAN;

        String getColourCode() {
            switch (this) {
                case RED:
                    return "\u001B[31m";
                case GREEN:
                    return "\u001B[32m";
                case YELLOW:
                    return "\u001B[33m";
                case BLUE:
                    return "\u001B[34m";
                case PURPLE:
                    return "\u001B[35m";
                case CYAN:
                    return "\u001B[36m";
                // black and everything else
                default:
                    return "\u001B[30m";
            }
        }

        static Colour getColour(String colour) {
            switch (colour.toUpperCase()) {
                case "RED":
                    return RED;
                case "BLUE":
                    return BLUE;
                case "YELLOW":
                    return YELLOW;
                case "GREEN":
                    return GREEN;
                case "CYAN":
                    return CYAN;
                case "PURPLE":
                    return PURPLE;
                default:
                    return BLACK;
            }
        }
    }
}
