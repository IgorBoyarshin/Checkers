package checkers.checker;

import javafx.scene.paint.Color;

/**
 * This enumeration describes the possible color schemes of the Checkers.
 * <p>
 * Created by Igor Boyarshin on April, 2017.
 */
public enum CheckerColor {
    /**
     * The white color scheme.
     */
    WHITE(Color.WHITE, Color.GRAY, Color.BLUE),

    /**
     * The black color scheme.
     */
    BLACK(Color.BLACK, Color.DARKGRAY, Color.BLUE),

    /**
     * The brown color scheme.
     */
    BROWN(Color.SANDYBROWN, Color.BROWN, Color.BLUE);

    /**
     * The primary color of the Checker(used for background).
     */
    public final Color primaryColor;

    /**
     * The secondary color of the Checker(used for decorations).
     */
    public final Color secondaryColor;

    /**
     * The selection color of the Checker(used when drawing a selected Checker).
     */
    public final Color selectionColor;

    /**
     * The constructor of the enumeration.
     *
     * @param primaryColor   the primary color of the scheme.
     * @param secondaryColor the secondary color of the scheme.
     * @param selectionColor the selection color of the scheme.
     */
    CheckerColor(Color primaryColor, Color secondaryColor, Color selectionColor) {
        this.primaryColor = primaryColor;
        this.secondaryColor = secondaryColor;
        this.selectionColor = selectionColor;
    }
}

