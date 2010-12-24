package AZBIrenderer;

import AZBIrenderer.Vector3.Point3d;

/**
 * A class for keeping all the information about a ray's intersection with a
 * surface
 * @author Adam Zeira
 */
public class IntersectionData {

    /**
     * The point of intersection
     */
    public @Point3d Vector3 point;
    /**
     * The surface's normal at the intersection point
     */
    public Vector3 normal;
    /**
     * A value satisfying the following identity (psedo code, for a Ray r):
     * <pre>r.origin + T * r.direction = this.Point </pre>
     */
    public float T;
    /*
     * the data of the surface hit
     */
    public SingleMaterialSurface surface;
    /**
     * The texture coordinates at the intersection
     */
    public float u, v;
    
    public IntersectionData() {
    }

    public void copyFrom (IntersectionData d)
    {
        this.point = d.point;
        this.normal = d.normal;
        this.surface = d.surface;
        this.T = d.T;
        this.u = d.u;
        this.v = d.v;
    }
}
