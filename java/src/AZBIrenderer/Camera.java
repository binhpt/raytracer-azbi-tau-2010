package AZBIrenderer;

import static AZBIrenderer.Vector3.*;

/**
 * 
 */
public class Camera implements ReflectionConstructed {

    public Camera() {
        this.screen_width = 2;
    }
    public Point3 eye;
    public Vector3 direction;
    public Point3 look_at;
    public Vector3 up_direction;
    public Vector3 right_direction;
    public float screen_dist;
    public float screen_width;
    public float screen_height;


    public void fillMissing() {
        if (this.direction == null && this.look_at != null) {
            this.direction = Normalize(sub(this.look_at, this.eye));
        }
        this.right_direction = Normalize(CrossProduct(this.up_direction, this.direction));
        this.up_direction = Normalize(CrossProduct(this.right_direction, this.direction));
    }
}
