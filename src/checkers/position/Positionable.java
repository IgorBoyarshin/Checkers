package checkers.position;

import checkers.util.Vector2d;

/**
 * This interface describes the common methods that each type of Position should have.
 * <p>
 * Created by Igor Boyarshin on April, 2017.
 */
public interface Positionable {
    /**
     * Translates the position stored in the class units into pixel units.
     *
     * @return the actual position in pixels.
     */
    Vector2d getActualPosition();

    /**
     * Updates the position(if needed) based on time elapsed since the start.
     * Can be left as is(no override).
     *
     * @param currentSecondsSinceStart seconds elapsed since the start of the game.
     */
    default void update(double currentSecondsSinceStart) {}
}
