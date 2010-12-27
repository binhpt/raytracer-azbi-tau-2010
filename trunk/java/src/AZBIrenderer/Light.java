package AZBIrenderer;

import AZBIrenderer.Vector3.Point3d;

/**
 * A base class for all light object
 * @author Adam Zeira & Barak Itkin
 */
public abstract class Light implements ReflectionConstructed {

    /**
     * The light's color
     */
    public Color color;

    public Light()
    {
        this.color = new Color(1, 1, 1, 1);
    }
    
    public abstract Color EffectFromLight(@Point3d Vector3 point);

    /**
     * Given a point, Fill a Ray object with a ray from the light source to that
     * point, and return the multiplier of the vector required to reach the light
     * @param point The point that should be reached by the ray
     * @param ray The ray to fill, with direction normalized
     * @return The multiplier of the vector required to reach the light
     */
    public abstract double GetRay(@Point3d Vector3 point, Ray ray);
}
