package AZBIrenderer;

import static AZBIrenderer.Vector3.*;

/**
 * A class for representing orthographic camera projections in a right hand
 * hand coordinate system.
 * <pre>
 * Right hand coordinate sytem | Java coordinate system
 * ----------------------------+--------------------------
 *           X                 |       *----->x
 *           ^                 |       |\
 *           | Z               |       | z
 *           |/                |       v
 *           *----->Y          |       y
 * </pre>
 * This means: yOurs = xJava, xOurs = -yJava
 *
 * @author Adam Zeira & Barak Itkin
 */
public class Camera implements ReflectionConstructed {

    public Camera() {
        this.screen_width = 2;
    }

    /**
     * The canera's origin (aka eye)
     */
    public Point3 eye;
    /**
     * The canera's forward direction vector
     */
    public Vector3 direction;
    /**
     * The point at which the camera is looking (Optional)
     */
    public Point3 look_at;
    /**
     * The vector of the screens up direction
     */
    public Vector3 up_direction;
    /**
     * The vector of the screens right direction
     */
    public Vector3 right_direction;
    /**
     * The distance of the image plane from the eye (aka f)
     */
    public float screen_dist;
    /**
     * The width of the screen in world units
     */
    public float screen_width;
    /**
     * The height of the screen in world units
     */
    public float screen_height;

    /**
     * Construct a ray for a given position in the image
     * @param xratio A variable between 0 to 1, specifying the image x
     *               coordinates in javas screen coordinate system
     * @param yratioA variable between 0 to 1, specifying the image y
     *               coordinates in javas screen coordinate system
     * @return The ray from the cameras eye which goes through that point
     */
    Ray CreateRay(float xratio, float yratio) {
        float xOurs = 1 - yratio;
        float yOurs = xratio;
        Ray r = new Ray();
        r.origin = this.eye;

        Vector3 dest = new Vector3(this.eye);
        dest = add(dest, mul(this.screen_dist, this.direction));
        dest = add(dest, mul((0.5f - xOurs) * this.screen_width, this.right_direction));
        dest = add(dest, mul((0.5f - yOurs) * this.screen_height, this.up_direction));

        r.direction = Normalize(sub(dest, this.eye));
        return r;
    }

    public void fillMissing() {
        /* If the direction isn't given, then we should initialize from the
         * "look at" point, which then should be given
         */
        if (this.direction == null) {
            if (this.look_at == null)
            {
                throw new IllegalArgumentException("The camera needs either a"
                        + " direction vector or a look at point. None of them"
                        + " was given!");
            }
            this.direction = Normalize(sub(this.look_at, this.eye));
        }
        this.right_direction = Normalize(CrossProduct(this.direction, this.up_direction));
        this.up_direction = Normalize(CrossProduct(this.right_direction, this.direction));
    }
}
