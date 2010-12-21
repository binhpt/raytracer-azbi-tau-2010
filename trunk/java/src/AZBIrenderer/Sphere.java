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
    public boolean Intersection(Ray r, IntersectionData intersect) {
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

    @Override
    public Color GetDiffuse(Vector3 point)
    {
        if (this.mtl_type.equals("flat"))
            return this.mtl_diffuse;

        //initial draft
        //checkers
        //float threshold = 1 / this.checkers_size;
        float theta = (float)Math.acos(point.z / this.radius);
        float phi = (float)Math.atan(point.y / point.x);

        int a = (int)Math.floor(theta / this.checkers_size) + (int)Math.floor(phi / this.checkers_size);

        if (a % 2 == 0)
            return new Color(0, 0, 0, 1);
        else
            return new Color(1, 1, 1, 1);
    }
}
