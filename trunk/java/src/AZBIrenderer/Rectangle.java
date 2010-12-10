/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package AZBIrenderer;

import static AZBIrenderer.Vector3.*;
/**
 *
 * @author user
 */
public class Rectangle extends Surface {

    public Point3 p0;
    public Point3 p1;
    public Point3 p2;
    public Point3 p3;

    public Vector3 u;
    public Vector3 v;
    public Vector3 normal;
    public float d, unormSquare, vnormSquare;

    @Override
    public boolean Intersection(Ray r, IntersectionData intersect) {
        float SABC = InnerProduct(r.direction, this.normal);

        if (SABC == 0)
            return false;

        intersect.T = - (d + InnerProduct(r.origin, normal)) / SABC;
        intersect.point = add(r.origin, mul (intersect.T, r.direction));

        Vector3 M = sub(intersect.point, p0);
        float temp1 = InnerProduct(M, u), temp2 = InnerProduct(M, u);
        temp1 *= temp1;
        temp2 *= temp2;
        if (! (0 <= temp1 && temp1 <= unormSquare && 0 <= temp2 && temp2 <= vnormSquare))
            return false;
        intersect.col = Debug.getFromNormal(this,normal);
        intersect.normal = new Vector3(normal);
/*
        Debug.print("*****************************************");
        Debug.print(this);
        Debug.print(r);
        Debug.print("Intersection at " + Debug.makeString(intersect.point));
        Debug.print("The distance is " + Debug.makeString(intersect.T));
        Debug.print("*****************************************");
*/
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
