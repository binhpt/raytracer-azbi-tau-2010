package AZBIrenderer;

import static AZBIrenderer.Vector3.*;

/**
 * A class for representing Disc sufraces
 * @author Barak Itkin
 */
public class Disc extends SingleMaterialSurface implements Surface {

    /**
     * The normal of the discs plane
     */
    public Vector3 normal;
    /**
     * The Discs center
     */
    public @Point3d Vector3 center;
    /**
     * The radius of the Disc
     */
    public float radius;
    /**
     * The variable d in the plane equation:
     * <pre>Ax + By + Cz + d = 0</pre>
     */
    public float d;

    @Override
    public boolean Intersection(Ray r, IntersectionData intersect) {
        if (!Math3D.RayPlanintersection(r, normal, d, intersect))
            return false;

        if (Norm(sub(intersect.point, this.center)) > radius)
            return false;

        intersect.normal = new Vector3(normal);
        intersect.surface = this;//Debug.getFromNormal(this, normal);

        return true;
    }

    @Override
    public AZBIrenderer.BoundingBox BoundingBox() {
        return new BoundingBox(
                Vector3.add (center, radius),
                Vector3.sub (center, radius));
    }

    @Override
    public void fillMissing() {
        this.d = - InnerProduct(center, normal);
        this.normal = Vector3.Normalize(normal);
    }
}
