package AZBIrenderer;

/**
 * 
 */
public class Camera {

    public Camera() {
    }
    
    public Vector3 eye;
    public Vector3 direction;
    public Vector3 look_at;
    public Vector3 up_direction;
    public Vector3 right_direction;
    public float screen_dist;
    public float screen_width;
    public float screen_height;

    /* bottom-left point */
    public Vector3 P1;
}
