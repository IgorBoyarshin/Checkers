package checkers.util;

/**
 * This class holds describes a two-dimensional integer Vector.
 * <p>
 * Created by Igor Boyarshin on April, 2017.
 */
public class Vector2i implements Cloneable {
    /**
     * The first variable
     */
    public int x;

    /**
     * The second variable
     */
    public int y;

    /**
     * The empty constructor, initializes variables with 0s.
     */
    public Vector2i() {
        this.x = 0;
        this.y = 0;
    }

    /**
     * This constructor initializes both variable with the same argument.
     *
     * @param arg the number with which to initialize the variables.
     */
    public Vector2i(int arg) {
        this.x = arg;
        this.y = arg;
    }

    /**
     * This constructor takes two numbers with which to initialize the variables.
     *
     * @param x the number with which to initialize the first variable
     * @param y the number with which to initialize the second variable
     */
    public Vector2i(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Returns the length of the vector.
     *
     * @return the length of the vector.
     */
    public int getLength() {
        return (int) Math.sqrt(x * x + y * y);
    }

    /**
     * Clones this Vector. Creates an identical new copy.
     *
     * @return an identical copy of this vector.
     */
    @Override
    public Vector2i clone() {
        return new Vector2i(x, y);
    }

    /**
     * Checks if this Vector is the same as the given object(Vector).
     *
     * @param object the object(Vector) with which to compare this Vector.
     * @return true if two Vectors are the same, false otherwise.
     */
    @Override
    public boolean equals(Object object) {
        if (object instanceof Vector2i) {
            final Vector2i vector = (Vector2i) object;
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
