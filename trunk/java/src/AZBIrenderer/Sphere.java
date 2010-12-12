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
        //d = Point2RayDist(this.center, r);
        //L_P2_P3_square = this.radius * this.radius - d * d;

        /* Now, make sure there is an intersection */
        /*if (L_P2_P3_square < 0 || L_P2_P3_square == Float.NaN)
        {
            return false;
        }

        L_P2_P1 = InnerProduct(sub(center, r.origin), r.direction);

	intersect.T = L_P2_P1 - (float) Math.sqrt(L_P2_P3_square);*/
	intersect.point = add(r.origin, mul (intersect.T, r.direction));
	intersect.normal = Normalize (sub (intersect.point, this.center));
        intersect.surface = this;// Debug.getFromNormal(this,intersect.normal); //change to something more complex next submission

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
