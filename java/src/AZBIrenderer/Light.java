package AZBIrenderer;

import java.util.List;

/**
 * A base class for all light object - will be filled later!
 */
public abstract class Light implements ReflectionConstructed {
    public Light()
    {
        this.color = new Color(1, 1, 1, 1);
    }
    
    public Color color;

    public abstract Color EffectFromLight(Point3 point);

    /*
     * fills ray with point and normalized vector, returns the multiplier of the vector
     * required to reach the light
     */
    public abstract float GetRay(Point3 point, Ray ray);
}
