import java.awt.*;

/**
 * Author: Jeroen
 * Date created: 17-11-16
 */
class User {

    private final String username;

    private final Color colour;

    User(String username, Color colour) {
        this.username = username;
        this.colour = colour;
    }

    String getUsername() {
        return username;
    }

    Color getColour() {
        return colour;
    }
}