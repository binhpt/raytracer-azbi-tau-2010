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

    /**
     * The "zero" angle when calculating the texture coordinates
     */
    public Vector3 TextureX0, TextureY0;

    @Override
    public boolean Intersection(Ray r, IntersectionData intersect, boolean doUV) {
        if (!Math3D.RayPlanintersection(r, normal, d, intersect))
            return false;

        float rad = Norm(sub(intersect.point, this.center));

        if (rad > radius)
            return false;

        intersect.normal = new Vector3(normal);
        intersect.surface = this;//Debug.getFromNormal(this, normal);

        if (doUV)
        {
            intersect.u = rad / radius;
            Vector3 d = Normalize(sub(intersect.point, this.center));
            float y = InnerProduct(d, this.TextureY0);
            float x = InnerProduct(d, this.TextureX0);
            intersect.v = ((float) Math.atan2(y, x) + Math3D.PI) * Math3D.INV_PI2;
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
    public void fillMissing() {
        this.d = - InnerProduct(center, normal);
        this.normal = Vector3.Normalize(normal);
            /* Some vector on the disc planen */
        this.TextureX0 = CrossProduct(this.normal, Math3D.Zaxis);
        float temp = Norm(this.TextureX0);
        /* If we picked something parallel to the direction in the cross-product
         * try again
         */
        if (temp == 0)
            this.TextureX0 = Normalize(CrossProduct(this.normal, Math3D.Xaxis));
        else
            this.TextureX0 = mul(1/temp, this.TextureX0);
        this.TextureY0 = Normalize(CrossProduct(this.normal, this.TextureX0));
    }
}
