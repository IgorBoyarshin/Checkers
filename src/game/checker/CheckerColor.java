package game.checker;

import javafx.scene.paint.Color;

/**
 * Created by Igorek on 09-Apr-17 at 8:56 AM.
 */
public enum CheckerColor {
    WHITE(Color.WHITE, Color.GRAY, Color.BLUE),
    BLACK(Color.BLACK, Color.DARKGRAY, Color.BLUE),
    BROWN(Color.SANDYBROWN, Color.BROWN, Color.BLUE);

    public final Color primaryColor;
    public final Color secondaryColor;
    public final Color selectionColor;

    CheckerColor(Color primaryColor, Color secondaryColor, Color selectionColor) {
        this.primaryColor = primaryColor;
        this.secondaryColor = secondaryColor;
        this.selectionColor = selectionColor;
    }
}

