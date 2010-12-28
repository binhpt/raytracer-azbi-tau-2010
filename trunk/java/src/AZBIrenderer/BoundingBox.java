package AZBIrenderer;

import java.util.List;
import java.util.Arrays;
import static AZBIrenderer.Vector3.*;

/**
 * A class for representing a 3D box, whose edges are parrallel to the X Y and Z
 * axises. This is going to be used later for some optimizations (such as octal
 * trees or other ideas we may implement).
 * @author Adam Zeira & Barak Itkin
 */
public class BoundingBox {

    /**
     * One corner of the bounding box
     */
    public @Point3d Vector3 max;
    /**
     * The other corner of the bounding box
     */
    public @Point3d Vector3 min;

    /**
     * Construct the box by specifying the two opposite corners
     */
    public BoundingBox(@Point3d Vector3 p1, @Point3d Vector3 p2) {
        this.max = CoordinateMax(p1, p2);
        this.min = CoordinateMin(p1, p2);
        Vector3 size = sub(max, min);
        // Enlarge the box by percent, or a constant factor - the bigger
        Vector3 eps = CoordinateMax(Math3D.GEOM_EPSILON_VEC, mul(Math3D.GEOM_EPSILON, size));
        this.max = add(eps, max);
        this.min = sub(min, eps);
    }

    /**
     * Copy the size and place from another box
     * @param other The other box
     */
    public void copyFrom (BoundingBox other) {
        this.min = other.min;
        this.max = other.max;
    }

    /**
     * Test whether two bounding boxes intersect. Based on the observation that
     * any two boxes which don't intersect, and are parallel to the axes, have
     * a coordinate T in which b.max.T < a.min.T
     * @param other The box to check the intersection with
     * @return Whether there is an intersection
     */
    public boolean intersects(BoundingBox other) {
        return !(other.max.x < this.min.x ||
                other.max.y < this.min.y ||
                other.max.z < this.min.z ||
                this.max.x < other.min.x ||
                this.max.y < other.min.y ||
                this.max.z < other.min.z);
    }

    /**
     * Check for the intersection of a ray and a bounding box.
     * @param r The ray to check
     * @return Whether there is an intersection
     */

    /*
     * The code is ugly, since it should be fast. It's done for every ray in the
     * scene, many times... But it's pretty intuitive :)
     * It can actually be up to 3 times faster in certain cases if written even
     * ugllier, but I think that there is a minimal level of readability which
     * is necessary here.
     */
    public boolean intersects(Ray r) {

        IntersectionData a = new IntersectionData(), b = new IntersectionData();

        /* Find the interrsection of the ray with the 6 cube planes
         * Note that if the ray does not intersect one plane orthogonal to the T
         * axis, it also does not intersect the other plane orthogonal to that axis
         */

        // Xaxis is the normal
        if (Math3D.RayPlanintersection(r, Math3D.Xaxis, -min.x, a))
        {
            Math3D.RayPlanintersection(r, Math3D.Xaxis, -max.x, b);
            if ((a.T > Math3D.GEOM_MINUS_EPSILON
                && min.y <= a.point.y && min.z <= a.point.z
                && max.y >= a.point.y && max.z >= a.point.z)
                ||
                (b.T > Math3D.GEOM_MINUS_EPSILON
                && min.y <= b.point.y && min.z <= b.point.z
                && max.y >= b.point.y && max.z >= b.point.z))
                return true;
        }

        // Yaxis is the normal
        if (Math3D.RayPlanintersection(r, Math3D.Yaxis, -min.y, a))
        {
            Math3D.RayPlanintersection(r, Math3D.Yaxis, -max.y, b);
            if ((a.T > Math3D.GEOM_MINUS_EPSILON
                && min.x <= a.point.x && min.z <= a.point.z
                && max.x >= a.point.x && max.z >= a.point.z)
                ||
                (b.T > Math3D.GEOM_MINUS_EPSILON
                && min.x <= b.point.x && min.z <= b.point.z
                && max.x >= b.point.x && max.z >= b.point.z))
                return true;
        }

        // Zaxis is the normal
        if (Math3D.RayPlanintersection(r, Math3D.Zaxis, -min.z, a))
        {
            Math3D.RayPlanintersection(r, Math3D.Zaxis, -max.z, b);
            if ((a.T > Math3D.GEOM_MINUS_EPSILON
                && min.x <= a.point.x && min.y <= a.point.y
                && max.x >= a.point.x && max.y >= a.point.y)
                ||
                (b.T > Math3D.GEOM_MINUS_EPSILON
                && min.x <= b.point.x && min.y <= b.point.y
                && max.x >= b.point.x && max.y >= b.point.y))
                return true;
        }

        return false;

    }
    /**
     * Construct a bounding box which contains all the given bounding boxes
     * @param boxes An array of boxes - <b>it's length must not be zero!</b>
     * @return A box containing all the given bounding boxes
     */
    public static BoundingBox create(BoundingBox... boxes)
    {
        return create(Arrays.asList(boxes));
    }

    /**
     * Construct a bounding box which contains all the given bounding boxes
     * @param boxes A list of boxes - <b>it's length must not be zero!</b>
     * @return A box containing all the given bounding boxes
     */
    public static BoundingBox create(List<BoundingBox> boxes)
    {
        Vector3 max = new Vector3(Double.MIN_VALUE, Double.MIN_VALUE, Double.MIN_VALUE);
        Vector3 min = new Vector3(Double.MAX_VALUE, Double.MAX_VALUE, Double.MAX_VALUE);

        for (BoundingBox box : boxes) {
            min = CoordinateMin(min, box.min);
            max = CoordinateMax(max, box.max);
        }
        return new BoundingBox(min, max);
    }
    /**
     * Construct a bounding box which contains all the given points
     * @param boxes An array of points - <b>it's length must not be zero!</b>
     * @return A box containing all the given points
     */
    public static BoundingBox create(@Point3d Vector3... pts)
    {
        @Point3d Vector3 max = new Vector3(Double.MIN_VALUE, Double.MIN_VALUE, Double.MIN_VALUE);
        @Point3d Vector3 min = new Vector3(Double.MAX_VALUE, Double.MAX_VALUE, Double.MAX_VALUE);

        for (@Point3d Vector3 pt : pts) {
            min = CoordinateMin(min, pt);
            max = CoordinateMax(max, pt);
        }
        return new BoundingBox(min, max);
    }

    @Override
    public String toString() {
        return "min: " + min.x + ", "+ min.y + ", " + min.z + " | " +
                "max: " + max.x + ", "+ max.y + ", " + max.z;
    }

}
