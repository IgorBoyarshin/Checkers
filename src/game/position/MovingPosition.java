package game.position;

import util.Vector2d;

/**
 * Created by Igorek on 09-Apr-17 at 9:10 AM.
 */
public class MovingPosition implements Positionable {

    /**
     * The position of the cell(in pixels) where the movement started
     */
    private final Vector2d positionStart;

    /**
     * The position of the cell(in pixels) where the movement must end
     */
    private final Vector2d positionEnd;

    /**
     * The time(in seconds) when the movement started
     */
    private final double movementTimeStartInSeconds;

    /**
     * The current actual position(in pixels)
     */
    private Vector2d currentPosition;

    /**
     * The movement speed of the position
     */
    private final MovementSpeed movementSpeed;

    /**
     * @param positionStart     the position(in pixels) where the movement started
     * @param positionEnd       the position(in pixels) where the movement must end
     * @param secondsSinceStart the time(in seconds) when the movement started
     */
    public MovingPosition(BoardPosition positionStart, BoardPosition positionEnd,
                          double secondsSinceStart, MovementSpeed movementSpeed) {
        super();
        this.positionStart = positionStart.getActualPosition();
        this.positionEnd = positionEnd.getActualPosition();
        this.movementTimeStartInSeconds = secondsSinceStart;
        this.movementSpeed = movementSpeed;

        this.currentPosition = positionStart.getActualPosition().clone();
    }

    /**
     * @return the actual position in pixels
     */
    @Override
    public Vector2d getActualPosition() {
        return currentPosition;
    }

    /**
     * Updates the actual position
     *
     * @param currentSecondsSinceStart seconds elapsed since the start of the game
     */
    @Override
    public void update(double currentSecondsSinceStart) {
        final double movementProgress = (currentSecondsSinceStart - movementTimeStartInSeconds) / movementSpeed.durationInSeconds;

        this.currentPosition.x = positionStart.x + (positionEnd.x - positionStart.x) * movementProgress;
        this.currentPosition.y = positionStart.y + (positionEnd.y - positionStart.y) * movementProgress;
    }
}
