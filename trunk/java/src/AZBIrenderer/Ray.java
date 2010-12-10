
package AZBIrenderer;

/**
 * THE DIRECTION MUST ALWAYS ALWAYS ALWAYS ALWAYS BE NORMALIZED!!!!!
 * 
 * @author Barak Itkin and Adam Zeira
 */
public class Ray {

    public Point3 origin;
    public Vector3 direction;

    public Ray (Point3 origin, Vector3 direction) {
        this.origin = origin;
        this.direction = Vector3.Normalize(direction);
    }

    public Ray() {
        this.origin = new Point3();
        this.direction = new Vector3();
    }

}
