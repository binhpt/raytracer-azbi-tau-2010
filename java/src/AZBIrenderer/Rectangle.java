package AZBIrenderer;

import static AZBIrenderer.Vector3.*;
/**
 * A wrapper class around {@link RectangleFace}
 * @author Adam Zeira & Barak Itkin
 * @see RectangleFace
 * @see Box
 */
public class Rectangle extends SingleMaterialSurface implements ReflectionConstructed, ReflectionWrapper {

    /**
     * A class for representing planar parallelograms in 3D space
     * 
     * <pre>
     *     p2         p3
     *     X----------X
     *    /          /
     *   /          /
     *  /          /
     * X----------X
     * p0         p1
     * </pre>
     *
     * It's seperated from the {@link Rectangle} class, since the {@link Box}
     * class also uses it.
     * @author Adam Zeira & Barak Itkin
     */
    public static class RectangleFace extends Face {
        public @Point3d Vector3 p0, p1, p2;

        /**
         * The vector from p0 to p1
         */
        public Vector3 v1;
        /**
         * The vector from p0 to p2
         */
        public Vector3 v0;
        /**
         * The surface's normal
         */
        public Vector3 normal;
        /**
         * The variable d in the plane equation:
         * <pre>Ax + By + Cz + d = 0</pre>
         */
        public float d;

        /**
         * Variables cached for performance
         */
        public float dot00, dot01, dot11, invDenom;

        public RectangleFace(SingleMaterialSurface sf,
                Vector3 p0, Vector3 p1, Vector3 p2) {
            super (sf);

            this.p0 = p0;
            this.p1 = p1;
            this.p2 = p2;

            this.v0 = sub(p2, p0);
            this.v1 = sub(p1, p0);

            this.dot00 = InnerProduct(this.v0, this.v0);
            this.dot01 = InnerProduct(this.v0, this.v1);
            this.dot11 = InnerProduct(this.v1, this.v1);

            this.normal = Normalize(CrossProduct(v0, v1));
            this.d = -InnerProduct(this.normal, this.p0);
        }

        public AZBIrenderer.BoundingBox BoundingBox() {
            return BoundingBox.create(p0, p1, p2, add(p1, v0));
        }

        public boolean Intersection(Ray r, IntersectionData intersect) {
            if (!Math3D.RayPlanintersection(r, normal, d, intersect))
                return false;

            @Point3d Vector3 P = intersect.point;

            Vector3 v2 = sub(P, p0);

            // Compute dot products
            float dot02 = InnerProduct(v0, v2);
            float dot12 = InnerProduct(v1, v2);

            // Compute barycentric coordinates
            invDenom = 1 / (dot00 * dot11 - dot01 * dot01);
            float u = (dot11 * dot02 - dot01 * dot12) * invDenom;
            float v = (dot00 * dot12 - dot01 * dot02) * invDenom;

            // Check if point is in triangle
            if (u < 0 || v < 0 || u > 1 || v > 1)
                return false;

            intersect.normal = new Vector3(-normal.x, -normal.y, -normal.z);
            intersect.surface = this.surfaceMaterial;

            return true;
        }
    }

    /**
     * The same variables from {@link RectangleFace}, present here only for the
     * reflection based parsing
     */
    public @Point3d Vector3 p0, p1, p2, p3;

    /**
     * The actual {@link RectangleFace} for this object, inside an array
     */
    public RectangleFace[] real;

    public Rectangle() { }

    public Object[] getRealObjects() {
        return real;
    }

    @Override
    public void fillMissing() {
        this.real = new RectangleFace[1];
        this.real[0] = new RectangleFace(this.makeCopyOfMaterial(), p0, p1, p2);
    }


}
