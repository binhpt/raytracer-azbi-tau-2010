package AZBIrenderer;

import static AZBIrenderer.Vector3.*;
/**
 * A class holding various 3D math utilities
 * @author Barak Itkin
 */
public class Math3D {

    public static final Vector3 Xaxis = new Vector3(1, 0, 0);
    public static final Vector3 Yaxis = new Vector3(0, 1, 0);
    public static final Vector3 Zaxis = new Vector3(0, 0, 1);

    public static final double PI = (double) Math.PI;
    public static final double PI2 = (double) (2 * Math.PI);
    public static final double INV_PI = (double) (1 / Math.PI);
    public static final double INV_PI2 = (double) (1 / (2 * Math.PI));

    public static final double GEOM_EPSILON = 0.03;
    public static final double GEOM_MINUS_EPSILON = -GEOM_EPSILON;

    public static final Vector3 GEOM_EPSILON_VEC = new Vector3(GEOM_EPSILON,GEOM_EPSILON,GEOM_EPSILON);

    /**
     * Compute the distance of a points from a ray
     */
    public static double Point2RayDist (Vector3 p, Ray r)
    {
        Vector3 M = sub(p,r.origin);
        double temp = InnerProduct(r.direction, M);
        double temp3 = InnerProduct(M, M) - temp*temp;
        double temp2 = Math.sqrt(Math.abs(temp3));
        return (double) temp2;
    }

    /**
     * Project a vector along a given normal, resulting a vector, which is on
     * the plane prepandicular to the normal that goes through the origin (the
     * plane goes through the origin)
     */
    /* Normal must be normalized!
     * Result is not normalized!
     */
    public static Vector3 flattenVec (Vector3 toFlat, Vector3 normal)
    {
        return sub(toFlat, mul(InnerProduct(toFlat, normal), normal));
    }

    /**
     * Project a point along a given normal, resulting a point, which is on
     * the plane prepandicular to the normal that goes through the origin (the
     * plane goes through the origin)
     */
    /* Normal must be normalized! */
    public static Vector3 flattenPt (Vector3 toFlat, Vector3 normal)
    {
        return sub(toFlat, mul(InnerProduct(toFlat, normal), normal));
    }

    /**
     * Project a ray along a given normal, resulting a ray, which is on
     * the plane prepandicular to the normal that goes through the origin (the
     * plane goes through the origin)
     */
    /* Normal must be normalized! */
    public static Ray flattenRay (Ray r, Vector3 normal)
    {
        return new Ray(flattenPt(r.origin, normal), Normalize(flattenVec(r.direction, normal)));
    }

    /**
     * Commonly used - compute the intersection of a ray and a plane.
     * @param r A ray
     * @param normal The normal of the plane
     * @param d The d parameter in the plane equation (<code>Ax+By+Cz+d=0</code>)
     * @param result An object to store the result in
     * @return Whether the plane and the ray intersect
     */
    public static boolean RayPlanintersection (Ray r, Vector3 normal, double d, IntersectionData result)
    {
        double SABC = InnerProduct(r.direction, normal);

        if (SABC == 0)
            return false;

        result.T = - (d + InnerProduct(r.origin, normal)) / SABC;
        result.point = add(r.origin, mul (result.T, r.direction));

        return true;
    }

    public double normalizeValue(double val, double rangeStart, double rangeEnd)
    {
        return (val - rangeStart) / (rangeEnd - rangeStart);
    }
}
