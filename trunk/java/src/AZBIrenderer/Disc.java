package AZBIrenderer;

import static AZBIrenderer.Vector3.*;

/**
 * A class for representing Disc sufraces
 * @author Barak Itkin
 */
public class Disc extends Surface {

    public Vector3 normal;
    /**
     * The Discs center
     */
    public Point3 center;
    /**
     * The radius of the Disc
     */
    public float radius;
    /**
     * The variable d in the plain equation:
     * <pre>Ax + By + Cz + d = 0</pre>
     */
    public float d;

    @Override
    public boolean Intersection(Ray r, IntersectionData intersect) {
        float SABC = InnerProduct(r.direction, this.normal);

        if (SABC == 0)
            return false;

        intersect.T = - (d + InnerProduct(r.origin, normal)) / SABC;
        intersect.point = add(r.origin, mul (intersect.T, r.direction));

        if (Norm(sub(intersect.point, this.center)) > radius)
            return false;

        intersect.normal = new Vector3(normal);
        intersect.col = Debug.getFromNormal(this, normal);

        return true;
    }

    @Override
    public AZBIrenderer.BoundingBox BoundingBox() {
        return new BoundingBox(
                Vector3.add (center, radius),
                Vector3.sub (center, radius));
    }

    public void fillMissing() {
        this.d = - InnerProduct(center, normal);
    }
}
