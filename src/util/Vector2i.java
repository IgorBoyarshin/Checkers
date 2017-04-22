package util;

/**
 * Created by Igorek on 09-Apr-17 at 9:02 AM.
 */
public class Vector2i implements Cloneable {
    public int x;
    public int y;

    public Vector2i() {
        this.x = 0;
        this.y = 0;
    }

    public Vector2i(int arg) {
        this.x = arg;
        this.y = arg;
    }

    public Vector2i(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getLength() {
        return (int) Math.sqrt(x * x + y * y);
    }

    @Override
    public Vector2i clone() {
        return new Vector2i(x, y);
    }

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

    @Override
    public String toString() {
        return "(x;y) = (" + x + ";" + y + ")";
    }
}
