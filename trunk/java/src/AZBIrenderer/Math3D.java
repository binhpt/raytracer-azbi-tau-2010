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

}
