package checkers.position;

import checkers.util.Vector2d;
import checkers.util.Vector2i;

import java.util.function.Consumer;

/**
 * Created by Igorek on 09-Apr-17 at 9:10 AM.
 */
public class MovingPosition implements Positionable {

    /**
     * The position of the cell(in pixels) where the movement started
     */
    private final BoardPosition positionStart;

    /**
     * The position of the cell where the movement must end
     */
    private final BoardPosition positionEnd;

    /**
     * The time(in seconds) when the movement started.
     * Gets initialized during the first call to the update() method
     */
    private double movementTimeStartInSeconds;

    /**
     * The current actual position(in pixels)
     */
    private Vector2d currentPosition;

    /**
     * The movement speed of the position
     */
    private final MovementSpeed movementSpeed;

    /**
     * Whether the movement has completed or not
     */
    private boolean completed;

    /**
     * @param positionStart the position(in pixels) where the movement started
     * @param positionEnd   the position(in pixels) where the movement must end
     */
    // TODO: movementSpeed
    public MovingPosition(BoardPosition positionStart, BoardPosition positionEnd,
                          MovementSpeed movementSpeed) {
        super();
        this.positionStart = positionStart;
        this.positionEnd = positionEnd;
        this.movementSpeed = movementSpeed;

        this.movementTimeStartInSeconds = 0.0;
//        this.currentPosition = new Vector2d(positionStart.x, positionStart.y);
        this.currentPosition = positionStart.getActualPosition().clone();
        this.completed = false;
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
        if (movementTimeStartInSeconds == 0.0) {
            // Initialize if this is the first call
            this.movementTimeStartInSeconds = currentSecondsSinceStart;
        }

        final double movementProgress = (currentSecondsSinceStart - movementTimeStartInSeconds) / movementSpeed.durationInSeconds;
        if (movementProgress < 1.0) {
            final Vector2d start = positionStart.getActualPosition();
            final Vector2d end = positionEnd.getActualPosition();

            this.currentPosition.x = 1.0 * start.x + (end.x - start.x) * movementProgress;
            this.currentPosition.y = 1.0 * start.y + (end.y - start.y) * movementProgress;
        } else {
            this.completed = true;
        }
    }

    public Vector2i getDestinationPosition() {
        return positionEnd.getPosition();
    }

    public boolean hasCompleted() {
        return completed;
    }
}
