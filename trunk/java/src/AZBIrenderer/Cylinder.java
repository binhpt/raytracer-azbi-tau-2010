package AZBIrenderer;

import static AZBIrenderer.Vector3.*;

/**
 * A class for representing Cylinder sufraces.
 * @author Barak Itkin
 */
public class Cylinder extends SingleMaterialSurface implements Surface {

    /**
     * Square root of 2, kept for preformance reasons
     */
    public static final double SQRT2 = Math.sqrt(2);

    /**
     * The direction of the cylinder
     */
    public Vector3 direction;
    /**
     * The Cylinders center
     */
    public @Point3d Vector3 start;
    /**
     * The radius of the Cylinder
     */
    public double radius;
    /**
     * The length of the Cylinder
     */
    public double length;

    /**
     * Kept for preformance reasons - see the mathematics file
     */
    public @Point3d Vector3 ProjectedCenter;

    /**
     * The "zero" angle when calculating the texture coordinates
     */
    public Vector3 TextureX0, TextureY0;

    /*
     * You will want to see the attached math file in order to understand the
     * intersection calculation - since the method used here is a bit unusual
     * (actually, it was invented by the writer of these lines).
     *
     * Note that when intersecting a ray with a cylinder, the second intersection may be
     * in range when the first isn't....
     */
    @Override
    public boolean Intersection(Ray r, IntersectionData intersect, boolean doUV) {
        double T1, T2;
        Ray projectedRay = Math3D.flattenRay(r, direction);
        Vector3 toStart;
        double len;

        double flattenedSize = Norm(Math3D.flattenVec(r.direction, direction));

        {
            //squared radius, squared distance
            double d_square, R_square, P3P2_length, P1P2_length;
            Vector3 P1P0;

            P1P0 = sub(Math3D.flattenPt(this.start, this.direction), projectedRay.origin);
            P1P2_length = InnerProduct(P1P0, projectedRay.direction);

            // In a cylinder, we actually interested in the intersection, even
            // if for a sphere it was a negative T, since the second intersection
            // may actually have a positive T.
            // if (P1P2_length < 0) return false;

            d_square = InnerProduct(P1P0, P1P0) - P1P2_length * P1P2_length;
            R_square = this.radius * this.radius;
            if (d_square > R_square) return false;

            P3P2_length = Math.sqrt(R_square - d_square);

            T1 = P1P2_length - P3P2_length;
            T2 = P1P2_length + P3P2_length;
        }

        intersect.T = T1 / flattenedSize;
        intersect.point = add(r.origin, mul(intersect.T, r.direction));
        toStart = sub(intersect.point, start);
        len = InnerProduct(toStart, direction);

        /* See if the first intersection works: */
        if (len < 0 || len > length)
        {
                intersect.T = T2 / flattenedSize;
                intersect.point = add(r.origin, mul(intersect.T, r.direction));
                toStart = sub(intersect.point, start);
                len = InnerProduct(toStart, direction);
                if (len < 0 || len > length)
                    return false;
        }
        intersect.normal = Normalize(Math3D.flattenVec(toStart, direction));
        intersect.surface = this;

        if (doUV)
        {
            intersect.u = len / this.length;
            Vector3 d = Normalize(sub(intersect.point, this.start));
            double y = InnerProduct(d, this.TextureY0);
            double x = InnerProduct(d, this.TextureX0);
            intersect.v = ( Math.atan2(y, x) + Math3D.PI) * Math3D.INV_PI2;
        }


        return true;
    }

    @Override
    public AZBIrenderer.BoundingBox BoundingBox() {
        Vector3 top = add(start, mul(length, direction));

        return BoundingBox.create(
                Vector3.add (start, radius),
                Vector3.sub (start, radius),
                Vector3.add (top, radius),
                Vector3.sub (top, radius));
    }

    @Override
    public void fillMissing() {
        double temp;
        this.ProjectedCenter = Math3D.flattenPt(start, direction);
        /* Some vector on the plane orthogonal to the cylinder's direction */
        this.TextureX0 = CrossProduct(this.direction, Math3D.Zaxis);
        temp = Norm(this.TextureX0);
        /* If we picked something parallel to the direction in the cross-product
         * try again
         */
        if (temp == 0)
            this.TextureX0 = Normalize(CrossProduct(this.direction, Math3D.Xaxis));
        else
            this.TextureX0 = mul(1/temp, this.TextureX0);
        this.TextureY0 = Normalize(CrossProduct(this.direction, this.TextureX0));
    }
}
