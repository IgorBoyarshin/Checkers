package checkers;

import checkers.position.MovementSpeed;

/**
 * This class describes the application settings
 * <p>
 * Created by Igor Boyarshin on April, 2017.
 */
public class CheckersSettings {

    /**
     * The instance of the CheckersSettings.
     * Can be created only by the CheckersWindow.
     */
    public static CheckersSettings instance;

    /**
     * The title of the window.
     */
    public final String windowTitle;

    /**
     * The height of the window.
     */
    public final int windowHeight;

    /**
     * The width of the window.
     */
    public final int windowWidth;

    /**
     * The size of the board in pixels.
     */
    public final double boardSizeInPixels;

    /**
     * Whether eating is mandatory.
     */
    public final boolean isEatingMandatory;

    /**
     * The size of a cell in pixels.
     */
    public final double cellSize;

    /**
     * The movement speed of a Checker.
     */
    public final MovementSpeed movementSpeed;

    /**
     * The constructor of the class.
     *
     * @param windowTitle       te title of the window
     * @param windowWidth       the width of the window.
     * @param windowHeight      the height of the window.
     * @param boardSizeInPixels the size of the board in pixels.
     * @param cellSize          the size of a cell in pixels.
     * @param isEatingMandatory whether eating is mandatory.
     * @param movementSpeed     the movement speed of a Checker.
     */
    CheckersSettings(
            String windowTitle,
            int windowWidth,
            int windowHeight,
            double boardSizeInPixels,
            double cellSize,
            boolean isEatingMandatory,
            MovementSpeed movementSpeed
    ) {
        this.windowTitle = windowTitle;
        this.windowHeight = windowHeight;
        this.windowWidth = windowWidth;
        this.boardSizeInPixels = boardSizeInPixels;
        this.isEatingMandatory = isEatingMandatory;
        this.cellSize = cellSize;
        this.movementSpeed = movementSpeed;

        instance = this;
    }

    /**
     * Retunrs the only instance of this class.
     *
     * @return the instance of this class.
     */
    // TODO: mb name just get()
    public static CheckersSettings getInstance() {
        return instance;
    }
}
