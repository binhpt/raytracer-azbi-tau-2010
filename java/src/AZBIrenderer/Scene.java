package AZBIrenderer;

import java.awt.image.BufferedImage;

/**
 * A class for representing scenes and their settings
 * @author Adam Zeira & Barak Itkin
 */
public class Scene implements ReflectionConstructed {

    public Scene() {
        this.background_col = new Color (0, 0, 0, 0);
        this.background_tex = null;
        this.ambient_light = new Color (0, 0, 0, 0);
        this.super_samp_width = 1;
        this.use_acceleration = false;
        this.mc_path_trace = false;
        this.max_ray_bounce = 3;
    }
    
    public Color background_col;
    public @FileTexture BufferedImage background_tex;
    public Color ambient_light;
    public int super_samp_width;
    public boolean use_acceleration;
    public boolean mc_path_trace;
    public int mc_path_trace_rec;
    public int mc_path_trace_num;
    /**
     * Defines the maximal amount of ray bounces
     */
    public int max_ray_bounce;

    public void fillMissing() { }
}
