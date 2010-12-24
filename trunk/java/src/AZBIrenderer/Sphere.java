package AZBIrenderer;

import static AZBIrenderer.Vector3.*;

/**
 * A class for representing sphere sufraces.
 * @author Adam Zeira & Barak Itkin
 */
public class Sphere extends SingleMaterialSurface implements Surface {

    /**
     * The spheres center
     */
    public @Point3d Vector3 center;
    /**
     * The radius of the sphere
     */
    public float radius;

    @Override
    public boolean Intersection(Ray r, IntersectionData intersect, boolean doUV) {
         //squared radius, squared distance
        float d_square, R_square, P3P2_length, P1P2_length;
        Vector3 P1P0;

        P1P0 = sub(this.center, r.origin);
        P1P2_length = InnerProduct(P1P0, r.direction);

        if (P1P2_length < 0) return false;

        d_square = InnerProduct(P1P0, P1P0) - P1P2_length * P1P2_length;
        R_square = this.radius * this.radius;
        if (d_square > R_square) return false;

        P3P2_length = (float)Math.sqrt(R_square - d_square);

        intersect.T = P1P2_length - P3P2_length;
	intersect.point = add(r.origin, mul (intersect.T, r.direction));
	intersect.normal = Normalize (sub (intersect.point, this.center));
        intersect.surface = this;

        if (doUV)
        {
            Vector3 d = Normalize(sub(intersect.point, this.center));
            float z = InnerProduct(d, Math3D.Zaxis);
            float y = InnerProduct(d, Math3D.Yaxis);
            float x = InnerProduct(d, Math3D.Xaxis);
            intersect.u = ((float) Math.atan2(y, x) + Math3D.PI) * Math3D.INV_PI2;
            intersect.v = (float) Math.acos((intersect.point.z - this.center.z) / this.radius) * Math3D.INV_PI;
        }

        return true;
    }

    @Override
    public AZBIrenderer.BoundingBox BoundingBox() {
        return new BoundingBox(
                Vector3.add (center, radius),
                Vector3.sub (center, radius));
    }

    @Override
    public void fillMissing() { }
}
