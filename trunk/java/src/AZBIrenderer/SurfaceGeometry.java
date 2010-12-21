package AZBIrenderer;

import static AZBIrenderer.Vector3.*;
/**
 * A base interface for geometric surfaces
 * @author Barak Itkin and Adam Zeira
 */
public interface SurfaceGeometry {

    /**
     * Compute the bounding box containing this object
     * @return A bounding box containing this object
     */
    BoundingBox BoundingBox();

    /**
     * Compue the first intersection of a ray with this surface
     * @param r The ray to intersect with
     * @param intersect A location for storing the intersection data in
     * @return Whether there is an intersection
     */
    boolean Intersection(final Ray r, IntersectionData intersect);
}
