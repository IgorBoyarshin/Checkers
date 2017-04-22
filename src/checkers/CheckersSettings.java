package checkers;

/**
 * Created by Igorek on 22-Apr-17 at 7:49 PM.
 */
public class CheckersSettings {

    private static CheckersSettings instance;

    public final String windowTitle;
    public final int windowHeight;
    public final int windowWidth;
    public final double boardSizeInPixels;

    public final boolean isEatingMandatory;
    public final double cellSize;
//    public final double boardSizeInCells;

    protected CheckersSettings(
            String windowTitle,
            int windowWidth,
            int windowHeight,
            double boardSizeInPixels,
//            double boardSizeInCells,
            double cellSize,
            boolean isEatingMandatory
    ) {
        this.windowTitle = windowTitle;
        this.windowHeight = windowHeight;
        this.windowWidth = windowWidth;
        this.boardSizeInPixels = boardSizeInPixels;
        this.isEatingMandatory = isEatingMandatory;
        this.cellSize = cellSize;
//        this.boardSizeInCells = boardSizeInCells;

        instance = this;
    }

    // TODO: mb name just get()
    public static CheckersSettings getInstance() {
        return instance;
    }
}
