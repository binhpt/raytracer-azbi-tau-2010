
package AZBIrenderer;

/**
 *
 * @author Barak Itkin and Adam Zeira
 */
public class Ray {

    Vector3 origin;
    Vector3 direction;

    public Ray (Vector3 origin, Vector3 direction) {
        this.origin = origin;
        this.direction = direction;
    }

    public Ray() {
        this.origin = new Vector3();
        this.direction = new Vector3();
    }

}
