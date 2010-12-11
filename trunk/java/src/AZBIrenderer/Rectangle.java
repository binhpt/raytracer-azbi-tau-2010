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
public class Rectangle extends Surface {

    public Point3 p0;
    public Point3 p1;
    public Point3 p2;
    public Point3 p3;

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
    public float unormSquare;
    /**
     * A value cached for performance reasons
     * <pre>Innerproduct(v,v)</pre>
     */
    public float vnormSquare;

    public Rectangle() {
    }

    public Rectangle(Point3 p0, Point3 p1, Point3 p2) {
        this.p0 = p0;
        this.p1 = p1;
        this.p2 = p2;
    }

    @Override
    public boolean Intersection(Ray r, IntersectionData intersect) {
        float SABC = InnerProduct(r.direction, this.normal);

        if (SABC == 0)
            return false;

        intersect.T = - (d + InnerProduct(r.origin, normal)) / SABC;
        intersect.point = add(r.origin, mul (intersect.T, r.direction));

        Vector3 M = sub(intersect.point, p0);
        float temp1 = InnerProduct(M, u), temp2 = InnerProduct(M, v);
        if (temp1 < 0 || temp2 < 0 || temp1 > unormSquare || temp2 > vnormSquare)
            return false;
        intersect.surface = this;//Debug.getFromNormal(this,normal);
        intersect.normal = new Vector3(normal);

        return true;

    }

    @Override
    public AZBIrenderer.BoundingBox BoundingBox() {
        return new BoundingBox(
                CoordinateMax(p0, CoordinateMax(p1, CoordinateMax(p2, p3))),
                CoordinateMin(p0, CoordinateMin(p1, CoordinateMin(p2, p3)))
                );
    }

    @Override
    public void fillMissing() {
        this.u = sub (p1, p0);
        this.v = sub (p2, p0);
        this.normal = Normalize(CrossProduct(u, v));
        this.d = - InnerProduct(p0, normal);
        this.unormSquare = InnerProduct(u, u);
        this.vnormSquare = InnerProduct(v, v);
        this.p3 = new Point3(add(p1, v));
    }

}
