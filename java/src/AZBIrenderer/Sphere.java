
package AZBIrenderer;

import static AZBIrenderer.Vector3.*;
import static java.lang.Math.*;
/**
 *
 */
public class Sphere extends Surface {

    public Vector3 center;
    public float radius;

    @Override
    public boolean Intersection(Ray r, IntersectionData intersect) {
	Vector3 L;
	float Tca, Thc, d, T;

	L = sub(center, r.origin);
	Tca = InnerProduct(L, r.direction);
	if (Tca < 0) return false;

	d = InnerProduct(L, L) + Tca * Tca;
	if (d > radius * radius) return false;

	Thc = (float)sqrt(radius * radius - d);
	/* assuming T won't be negative, which should never happen in cases that matter (either we're inside the sphere and so we have bigger problems, or it's behind us)*/
	T = min(Tca - Thc, Tca + Thc);

	intersect.point = (Vector3)mul(T, r.direction);
	intersect.col = this.mtl_diffuse; //change to something more complex next submission
	intersect.normal = sub (intersect.point, this.center);
	intersect.T = T;

	return true;
    }

    @Override
    public AZBIrenderer.BoundingBox BoundingBox() {
        return new BoundingBox(
                Vector3.add (center, radius),
                Vector3.sub (center, radius));
    }
}
