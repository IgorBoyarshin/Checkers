package util;

/**
 * Created by Igorek on 09-Apr-17 at 9:02 AM.
 */
public class Vector2d implements Cloneable {
    public double x;
    public double y;

    public Vector2d() {
        this.x = 0.0;
        this.y = 0.0;
    }

    public Vector2d(double arg) {
        this.x = arg;
        this.y = arg;
    }

    public Vector2d(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getLength() {
        return Math.sqrt(x * x + y * y);
    }

    @Override
    public Vector2d clone() {
        return new Vector2d(x, y);
    }

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

    @Override
    public String toString() {
        return "(x;y) = (" + x + ";" + y + ")";
    }
}
