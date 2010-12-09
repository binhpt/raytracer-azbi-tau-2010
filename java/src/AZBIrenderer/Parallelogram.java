
package AZBIrenderer;

import static java.lang.Math.*;
import static AZBIrenderer.Vector3.*;
/**
 *
 */
public class Parallelogram extends Surface {

    // TODO: support this!
    @Override
    public boolean Intersection(Ray r, IntersectionData intersect) {
        return false;
    }

    @Override
    public BoundingBox BoundingBox() {
        return new BoundingBox(
                CoordinateMin(CoordinateMin(p0, p1), p2),
                CoordinateMax(CoordinateMax(p0, p1), p2)
                );
    }

    Vector3 p0;
    Vector3 p1;
    Vector3 p2;
}
