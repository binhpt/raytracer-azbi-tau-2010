
package AZBIrenderer;

import static AZBIrenderer.Vector3.*;
import static AZBIrenderer.Math3D.*;
/**
 *
 */
public class Sphere extends Surface {

    public Point3 center;
    public float radius;

    @Override
    public boolean Intersection(Ray r, IntersectionData intersect) {
        float L_P2_P3_square, L_P2_P1, d;

        d = Point2RayDist(this.center, r);
        L_P2_P3_square = this.radius * this.radius - d * d;

        /* Now, make sure there is an intersection */
        if (L_P2_P3_square < 0 || L_P2_P3_square == Float.NaN)
        {
//            intersect.point = null;
//            intersect.col = null; //change to something more complex next submission
//            intersect.normal = null;
//            intersect.T = Float.POSITIVE_INFINITY;
            return false;
        }

        L_P2_P1 = InnerProduct(sub(center, r.origin), r.direction);

	intersect.T = L_P2_P1 - (float) Math.sqrt(L_P2_P3_square);
	intersect.point = add(r.origin, mul (intersect.T, r.direction));
	intersect.normal = Normalize (sub (intersect.point, this.center));
        intersect.col = Debug.getFromNormal(this,intersect.normal); //change to something more complex next submission
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
                Vector3.add (center, radius),
                Vector3.sub (center, radius));
    }

    public void fillMissing() { }
}
