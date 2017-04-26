package checkers.board;

import javafx.scene.paint.Color;

/**
 * This enumeration describes the possible color schemes for the cells of the Board.
 * <p>
 * Created by Igor Boyarshin on April, 2017.
 */
public enum BoardCellColor {
    /**
     * The brown color scheme.
     */
    BROWN(Color.WHITE, Color.BROWN),

    /**
     * The black color scheme.
     */
    BLACK(Color.WHITE, Color.BLACK);

    /**
     * The color of the light cells of the Board.
     */
    public final Color lightColor;

    /**
     * The color of the dark cells of the Board.
     */
    public final Color darkColor;

    /**
     * The constructor of the enumeration.
     *
     * @param lightColor the color of the light cells of the Board.
     * @param darkColor  the color of the dark cells of the Board.
     */
    BoardCellColor(Color lightColor, Color darkColor) {
        this.lightColor = lightColor;
        this.darkColor = darkColor;
    }
}
