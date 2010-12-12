package AZBIrenderer;

import static AZBIrenderer.Vector3.*;
/**
 * A class for representing planar parallelograms in 3D space
 * <pre>
 *     p2         p3
 *     X----------X
 *    /          /
 *   /          /
 *  /          /
 * X----------X
 * p0         p1
 * </pre>
 * @author Adam Zeira & Barak Itkin
 */
public class Rectangle extends SingleMaterialSurface implements ReflectionConstructed, ReflectionWrapper {

    public static class RectangleFace extends Face {
        public Point3 p0, p1, p2;

        /**
         * The vector from p0 to p1
         */
        public Vector3 u;
        /**
         * The vector from p0 to p2
         */
        public Vector3 v;
        /**
         * The surface's normal
         */
        public Vector3 normal;
        /**
         * The variable d in the plain equation:
         * <pre>Ax + By + Cz + d = 0</pre>
         */
        public float d;
        /**
         * A value cached for performance reasons
         * <pre>Innerproduct(u,u)</pre>
         */
        public float uNormSquare;
        /**
         * A value cached for performance reasons
         * <pre>Innerproduct(v,v)</pre>
         */
        public float vNormSquare;

        public RectangleFace(SingleMaterialSurface sf,
                Point3 p0, Point3 p1, Point3 p2) {
            super (sf);

            this.p0 = p0;
            this.p1 = p1;
            this.p2 = p2;

            this.u = sub(p1,p0);
            this.uNormSquare = InnerProduct(this.u, this.u);
            this.v = sub(p2,p0);
            this.vNormSquare = InnerProduct(this.v, this.v);

            this.normal = Normalize(CrossProduct(u, v));
            this.d = -InnerProduct(this.normal, this.p0);
        }

        public AZBIrenderer.BoundingBox BoundingBox() {
            return BoundingBox.create(p0, p1, p2, add(p0, v));
        }

        public boolean Intersection(Ray r, IntersectionData intersect) {
            if (!Math3D.RayPlanintersection(r, normal, d, intersect))
                return false;

            Vector3 x = sub(intersect.point, p0);

            float temp1 = InnerProduct(x, u), temp2 = InnerProduct(x, v);

            if (temp1 < 0 || temp1 > uNormSquare || temp2 < 0 || temp2 > vNormSquare)
                return false;

            intersect.normal = normal;
            intersect.surface = this.surfaceMaterial;

            return true;
        }
    }

    public Point3 p0;
    public Point3 p1;
    public Point3 p2;
    public Point3 p3;

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
