package AZBIrenderer;

import static AZBIrenderer.Vector3.*;
/**
 * A class holding various 3D math utilities
 * @author Barak Itkin
 */
public class Math3D {

    public static float Point2RayDist (Vector3 p, Ray r)
    {
        Vector3 M = sub(p,r.origin);
        float temp = InnerProduct(r.direction, M);
        float temp3 = InnerProduct(M, M) - temp*temp;
        double temp2 = Math.sqrt(Math.abs(temp3));
        return (float) temp2;
    }
    /* Normal must be normalized!
     * Result is nor normalized!
     */
    public static Vector3 flattenVec (Vector3 toFlat, Vector3 normal)
    {
        return sub(toFlat, mul(InnerProduct(toFlat, normal), normal));
    }

    /* Normal must be normalized! */
    public static Point3 flattenPt (Point3 toFlat, Vector3 normal)
    {
        return new Point3(flattenVec(toFlat, normal));
    }

    /* Normal must be normalized! */
    public static Ray flattenRay (Ray r, Vector3 normal)
    {
        return new Ray(flattenPt(r.origin, normal), Normalize(flattenVec(r.direction, normal)));
    }

    public static boolean RayPlanintersection (Ray r, Vector3 normal, float d, IntersectionData result)
    {
        float SABC = InnerProduct(r.direction, normal);

        if (SABC == 0)
            return false;

        result.T = - (d + InnerProduct(r.origin, normal)) / SABC;
        result.point = add(r.origin, mul (result.T, r.direction));

        return true;
    }

}
