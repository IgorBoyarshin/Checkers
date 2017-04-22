package game.board;

import javafx.scene.paint.Color;

/**
 * Created by Igorek on 12-Apr-17 at 3:33 PM.
 */
public enum BoardCellColor {
    BROWN(Color.WHITE, Color.BROWN),
    BLACK(Color.WHITE, Color.BLACK);

    public final Color lightColor;
    public final Color darkColor;

    BoardCellColor(Color lightColor, Color darkColor) {
        this.lightColor = lightColor;
        this.darkColor = darkColor;
    }
}
