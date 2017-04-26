package checkers.util;

/**
 * This class holds a pair of objects that have the same type.
 * <p>
 * Created by Igor Boyarshin on April, 2017.
 */
public class Pair<T> {

    /**
     * The first object of the pair
     */
    public final T a;

    /**
     * The second object of the pair
     */
    public final T b;

    /**
     * Main constructor for the class.
     *
     * @param a the first object of the pair
     * @param b the second object of the pair
     */
    public Pair(T a, T b) {
        this.a = a;
        this.b = b;
    }
}
