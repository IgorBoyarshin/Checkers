package checkers.util;

/**
 * This class holds describes a two-dimensional double Vector.
 * <p>
 * Created by Igor Boyarshin on April, 2017.
 */
public class Vector2d implements Cloneable {
    /**
     * The first variable
     */
    public double x;

    /**
     * The second variable
     */
    public double y;

    /**
     * The empty constructor, initializes variables with 0s.
     */
    public Vector2d() {
        this.x = 0.0;
        this.y = 0.0;
    }

    /**
     * This constructor initializes both variable with the same argument.
     *
     * @param arg the number with which to initialize the variables.
     */
    public Vector2d(double arg) {
        this.x = arg;
        this.y = arg;
    }

    /**
     * This constructor takes two numbers with which to initialize the variables.
     *
     * @param x the number with which to initialize the first variable
     * @param y the number with which to initialize the second variable
     */
    public Vector2d(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Returns the length of the vector.
     *
     * @return the length of the vector.
     */
    public double getLength() {
        return Math.sqrt(x * x + y * y);
    }

    /**
     * Clones this Vector. Creates an identical new copy.
     *
     * @return an identical copy of this vector.
     */
    @Override
    public Vector2d clone() {
        return new Vector2d(x, y);
    }

    /**
     * Checks if this Vector is the same as the given object(Vector).
     *
     * @param object the object(Vector) with which to compare this Vector.
     * @return true if two Vectors are the same, false otherwise.
     */
    @Override
    public boolean equals(Object object) {
        if (object instanceof Vector2d) {
            final Vector2d vector = (Vector2d) object;
            if (vector.x == this.x && vector.y == this.y) {
                return true;
            }
        }

        return false;
    }

    /**
     * A String representation of this Vector.
     *
     * @return the String representation of this Vector.
     */
    @Override
    public String toString() {
        return "(x;y) = (" + x + ";" + y + ")";
    }
}
