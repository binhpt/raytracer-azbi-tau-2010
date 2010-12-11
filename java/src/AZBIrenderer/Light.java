package AZBIrenderer;

import java.util.List;

/**
 * A base class for all light object - will be filled later!
 */
public abstract class Light implements ReflectionConstructed {
    public Light()
    {
        this.intensity = new Color(1, 1, 1, 1);
    }
    
    public Color intensity;

    public abstract Color EffectFromLight(Point3 point);
    public abstract Ray GetRay(Point3 point);
}
