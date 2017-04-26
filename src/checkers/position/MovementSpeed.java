package checkers.position;

/**
 * This enumeration describes possible movement speeds for a Checker.
 * This speed is used in MovingPosition class in order to calculate the actual position over time.
 * <p>
 * Created by Igor Boyarshin on April, 2017.
 */
public enum MovementSpeed {
    /**
     * Slow movement speed.
     */
    SLOW(1.0),

    /**
     * Medium movement speed.
     */
    MEDIUM(0.8),

    /**
     * Fast movement speed.
     */
    FAST(0.5);

    /**
     * The duration in seconds of the movement
     */
    public final double durationInSeconds;

    /**
     * The constructor for this enumeration. Initializes the durationInSeconds variable.
     *
     * @param durationInSeconds the duration in seconds of the movement.
     */
    MovementSpeed(double durationInSeconds) {
        this.durationInSeconds = durationInSeconds;
    }
}
