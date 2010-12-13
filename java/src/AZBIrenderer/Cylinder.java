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
    public static final float SQRT2 = (float)Math.sqrt(2);

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
    public float radius;
    /**
     * The length of the Cylinder
     */
    public float length;

    public Sphere ProjectedSphere;

    /*
     * You will want to see the attached math file in order to understand the
     * intersection calculation - since the method used here is a bit unusual
     * (actually, it was invented by the writer of these lines).
     *
     * Note that when intersecting a ray with a cylinder, the second intersection may be
     * in range when the first isn't....
     */
    @Override
    public boolean Intersection(Ray r, IntersectionData intersect) {
        IntersectionData temp = new IntersectionData();
        Ray projectedRay = Math3D.flattenRay(r, direction);
        Vector3 toStart;
        float len;

        float flattenedSize = Norm(Math3D.flattenVec(r.direction, direction));

        if (!ProjectedSphere.Intersection(projectedRay, temp))
            return false;

        intersect.T = temp.T / flattenedSize;
        intersect.point = add(r.origin, mul(intersect.T, r.direction));
        toStart = sub(intersect.point, start);
        len = InnerProduct(toStart, direction);

        /* See if the first intersection works: */
        if (len < 0 || len > length)
        {
                /* Try the second intersection */
            float d = Math3D.Point2RayDist(ProjectedSphere.center, projectedRay);
            float P2P3_flat_square = (radius * radius - d * d);
            float projDiff_square = 4 * P2P3_flat_square * (1 * 1 - flattenedSize * flattenedSize);
                intersect.T += Math.sqrt(4 * P2P3_flat_square + projDiff_square);
                intersect.point = add(r.origin, mul(intersect.T, r.direction));
                toStart = sub(intersect.point, start);
                len = InnerProduct(toStart, direction);
                if (len < 0 || len > length)
                    return false;
        }
        intersect.normal = Normalize(Math3D.flattenVec(toStart, direction));
        intersect.surface = this;
        //intersect.col = Debug.getFromNormal(this, intersect.normal);

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
        this.ProjectedSphere = new Sphere();
        this.ProjectedSphere.center = sub(start, mul(InnerProduct(direction, start), direction));
        this.ProjectedSphere.radius = radius;
        this.ProjectedSphere.fillMissing();
    }
}
