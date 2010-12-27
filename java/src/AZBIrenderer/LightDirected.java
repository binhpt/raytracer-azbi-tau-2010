package AZBIrenderer;

import AZBIrenderer.Vector3.Point3d;

/**
 * A class for representing directed lights (aka Sun lights), which are
 * equivalent to point lights in an infinite distance
 * @author Adam Zeira
 */
public class LightDirected extends Light{

    public Color EffectFromLight(@Point3d Vector3 p)
    {
        return this.color;
    }

    //OPTIMIZE
    public double GetRay(@Point3d Vector3 point, Ray ray)
    {
        ray.origin = point;
        ray.direction = reverseDirection;
        return Double.POSITIVE_INFINITY;
    }
    
    public Vector3 direction;
    public Vector3 reverseDirection; //for efficiency

    public void fillMissing()
    {
        direction = Vector3.Normalize(direction);
        reverseDirection = new Vector3(-direction.x, -direction.y, -direction.z);
    }
}
