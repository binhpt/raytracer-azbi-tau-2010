
package AZBIrenderer;

import AZBIrenderer.Vector3.Point3d;

/**
 * A class for representing a ray in 3D space.
 * <b>THE DIRECTION MUST ALWAYS ALWAYS ALWAYS ALWAYS BE NORMALIZED!!!!!</b>
 * That is because the implementation of other classes highly depend on this!
 * Also, <b>The sign of the direction vector matters!</b>
 * @author Barak Itkin and Adam Zeira
 */
public class Ray {

    /**
     * The origin point of the Ray
     */
    @Point3d
    public Vector3 origin;
    /**
     * The direction vector of the Ray
     */
    public Vector3 direction;

    public Ray (@Point3d Vector3 origin, Vector3 direction) {
        this.origin = origin;
        this.direction = Vector3.Normalize(direction);
    }

    public Ray() {
        this.origin = new Vector3();
        this.direction = new Vector3();
    }

}
