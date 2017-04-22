package checkers.position;

/**
 * This enumeration describes possible movement speeds for a Checker.
 * This speed is used in MovingPosition class in order to calculate the actual position
 *
 * Created by Igorek on 12-Apr-17 at 4:14 PM.
 */
public enum MovementSpeed {
    SLOW(1.0),
    MEDIUM(0.8),
    FAST(0.5);

    public final double durationInSeconds;

    MovementSpeed(double durationInSeconds) {
        this.durationInSeconds = durationInSeconds;
    }
}
