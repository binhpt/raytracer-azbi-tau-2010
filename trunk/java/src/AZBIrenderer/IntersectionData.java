package AZBIrenderer;

/**
 *
 */
public class IntersectionData {

    public Vector3 point;
    public Vector3 normal;
    public Color col;
    public float T;

    public IntersectionData() {
    }

    public IntersectionData(Vector3 point, Vector3 normal, Color col, float T) {
        this.point = point;
        this.normal = normal;
        this.col = col;
        this.T = T;
    }
}
