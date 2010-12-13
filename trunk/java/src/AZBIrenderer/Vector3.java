package AZBIrenderer;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Iterator;

/**
 * A class for representing vectors in 3D space. The data is saved inside 3
 * seperate fields and not in an array, since the referencing of the array may
 * have a performance cost for heavily used code (and besides, having fields
 * labeles by axis makes the code more readable)
 *
 * For the same reason exactly, as many methods as possible are not defined as
 * virtual - instead they are defined as static to skip the Dynamic Dispatch
 * mechanisem which is expensive.
 *
 * @author Adam Zeira & Barak Itkin
 */
public class Vector3 implements Iterable<Float> {

    /**
     * An annotation for Vector3 fields which specifies that they are points and
     * therefore should not be normalized
     */
    @Retention(RetentionPolicy.RUNTIME)
    public static @interface Point3d { }
    
    /**
     * The coordinates of the vector
     */
    public float x, y, z;

    /**
     * An array based constructor for a 3d vectore
     * @param c an array holding {x, y, z} of the vector
     */
    public Vector3 (float c[])
    {
        this.x = c[0];
        this.y = c[1];
        this.z = c[2];
    }

    /**
     * Initialize the vector directly
     * @param x the vectors x coordinate
     * @param y the vectors y coordinate
     * @param z the vectors z coordinate
     */
    public Vector3 (float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * A default constructor
     */
    public Vector3 () {
        this.x = 0;
        this.y = 0;
        this.z = 0;
    }

    /**
     * A copy constructor
     * @param v the source to copy
     */
    public Vector3 (Vector3 v) {
        this.x = v.x;
        this.y = v.y;
        this.z = v.z;
    }

    /**
     * Make vectors iterable, exactly like lists and arrays
     * @return An iterator for this vector
     */
    public Iterator<Float> iterator() {
        return new Iterator<Float>() {

            int count = -1;

            public boolean hasNext() {
                return count < 2;
            }

            public Float next() {
                switch (++count)
                {
                    case 0:
                        return x;
                    case 1:
                        return y;
                    case 2:
                        return z;
                    default:
                        throw new ArrayIndexOutOfBoundsException(count);
                }
            }

            public void remove() {
                throw new UnsupportedOperationException("You can't delete"
                        + " coordinates for Vector3 objects...");
            }
        };
    }
    

    /**
     * Vector addition
     * @return a new vector representing v1 + v2
     */
    public static Vector3 add (Vector3 v1, Vector3 v2)
    {
        return new Vector3 (
                v1.x + v2.x,
                v1.y + v2.y,
                v1.z + v2.z
                );
    }

    /**
     * Vector Scalar addition
     * @return a new vector representing v1 + f*(1, 1, 1)
     */
    public static Vector3 add (Vector3 v1, float f)
    {
        return new Vector3 (
                v1.x + f,
                v1.y + f,
                v1.z + f
                );
    }

    /**
     * Vector subtraction
     * @return a new vector representing v1 - v2
     */
    public static Vector3 sub (Vector3 v1, Vector3 v2)
    {
        return new Vector3 (
                v1.x - v2.x,
                v1.y - v2.y,
                v1.z - v2.z
                );
    }

    /**
     * Vector Scalar subtraction
     * @return a new vector representing v1 - f*(1, 1, 1)
     */
    public static Vector3 sub (Vector3 v1, float f)
    {
        return new Vector3 (
                v1.x - f,
                v1.y - f,
                v1.z - f
                );
    }

    /**
     * Vector Scalar multiplication
     * @return a new vector representing f * v1
     */
    public static Vector3 mul (float f, Vector3 v1)
    {
        return new Vector3 (
                v1.x * f,
                v1.y * f,
                v1.z * f
                );
    }

    /**
     * Compute the inner product (also known as dot product) of two vectors.
     * <pre>
     *   &lt;v1, v2&gt; = x1 * x2 + y1 * y2 + z1 * z2
     * </pre>
     * @return &lt;v1, v2&gt;
     */
    public static float InnerProduct(Vector3 v1, Vector3 v2)
    {
        return  v1.x * v2.x +
                v1.y * v2.y +
                v1.z * v2.z ;
    }

    /**
     * Compute the cross product of two vectors.
     * <pre>
     *           |X  Y  Z |   (y1 * z2 - z1 * y2)
     * v1 × v2 = |x1 y1 z1| = (z1 * x2 - x1 * z2)
     *           |x2 y2 z2|   (x1 * y2 - y1 * x2)
     * </pre>
     * @return v1 × v2
     */
    public static Vector3 CrossProduct(Vector3 v1, Vector3 v2)
    {
        return new Vector3(
                v1.y * v2.z - v1.z * v2.y,
                v1.z * v2.x - v1.x * v2.z,
                v1.x * v2.y - v1.y * v2.x
                );
    }

    /**
     * Normalize a vector
     * @param v A vector to normalize
     * @return v / ||v||
     */
    public static Vector3 Normalize(Vector3 v)
    {
        return mul (
                (float)(1 / Math.sqrt(InnerProduct(v, v))),
                v
                );
    }

    /**
     * Compute the norm (length) of a vector
     * @param v The vector to computer its length
     * @return the vectors length
     */
    public static float Norm(Vector3 v)
    {
        return (float) Math.sqrt(InnerProduct(v, v));
    }

    /**
     * Return a point whose coordinates are the coordinate minima:
     * x = min(v1.x, v2.x)
     * y = min(v1.y, v2.y)
     * z = min(v1.z, v2.z)
     */
    public static Vector3 CoordinateMin(Vector3 v1, Vector3 v2)
    {
        return new Vector3 (
                (v1.x < v2.x) ? v1.x : v2.x,
                (v1.y < v2.y) ? v1.y : v2.y,
                (v1.z < v2.z) ? v1.z : v2.z
                );
    }

    /**
     * Return a point whose coordinates are the coordinate maxima:
     * x = max(v1.x, v2.x)
     * y = max(v1.y, v2.y)
     * z = max(v1.z, v2.z)
     */
    public static Vector3 CoordinateMax(Vector3 v1, Vector3 v2)
    {
        return new Vector3 (
                (v1.x > v2.x) ? v1.x : v2.x,
                (v1.y > v2.y) ? v1.y : v2.y,
                (v1.z > v2.z) ? v1.z : v2.z
                );
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o)
            return true;

        if (!(o instanceof Vector3)) return false;

        Vector3 v = (Vector3)o;

        if (this.x == v.x && this.y == v.y && this.z == v.z)
            return true;

        return false;
    }
}
