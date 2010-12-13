package AZBIrenderer;

import static AZBIrenderer.Vector3.*;
import static AZBIrenderer.Math3D.*;

/**
 * A class for representing sphere sufraces
 * @author Adam Zeira & Barak Itkin
 */
public class Sphere extends SingleMaterialSurface implements Surface {

    /**
     * The spheres center
     */
    public Point3 center;
    /**
     * The radius of the sphere
     */
    public float radius;

    @Override
    public boolean Intersection(Ray r, IntersectionData intersect) {
        //squared radius, squared distance
        float sd, srad, thc, tca;
        Vector3 L;
        
        L = sub(this.center, r.origin);
        tca = InnerProduct(L, r.direction);

        if (tca < 0) return false;
        
        sd = InnerProduct(L, L) - tca * tca;
        srad = this.radius * this.radius;
        if (sd > srad) return false;

        thc = (float)Math.sqrt(srad - sd);

        intersect.T = tca - thc;
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

    public void fillMissing() { }
}
