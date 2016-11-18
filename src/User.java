import java.awt.*;

/**
 * Author: Jeroen
 * Date created: 17-11-16
 */
class User {

    private final String username;

    private final ColorOut.Colour colour;

    User(String username, ColorOut.Colour colour) {
        this.username = username;
        this.colour = colour;
    }

    String getUsername() {
        return username;
    }

    ColorOut.Colour getColour() {
        return colour;
    }
}