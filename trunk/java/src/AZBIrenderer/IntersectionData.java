package AZBIrenderer;

/**
 * A class for keeping all the information about a ray's intersection with a
 * surface
 * @author Adam Zeira
 */
public class IntersectionData {

    /**
     * The point of intersection
     */
    public Vector3 point;
    /**
     * The surface's normal at the intersection point
     */
    public Vector3 normal;
    /**
     * The surface's color at the intersection point
     */
    public Color col;
    /**
     * A value satisfying the following identity (psedo code, for a Ray r):
     * <pre>r.origin + T * r.direction = this.Point </pre>
     */
    public float T;

    public IntersectionData() {
    }

    public void copyFrom (IntersectionData d)
    {
        this.point = d.point;
        this.normal = d.normal;
        this.col = d.col;
        this.T = d.T;
    }
}
