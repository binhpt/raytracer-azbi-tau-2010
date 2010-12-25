
package AZBIrenderer;

import AZBIrenderer.Vector3.Point3d;

/**
 * A class for representing point lights
 * @author Adam Zeira
 */
public class LightPoint extends Light{
    public LightPoint()
    {
        super();
        attenuation = new Vector3(1, 0, 0);
    }

    @Override
    public Color EffectFromLight(@Point3d Vector3 point) {
        float d = (float)Math.sqrt(Math.pow(point.x - pos.x, 2) + Math.pow(point.y - pos.y, 2) + Math.pow(point.z - pos.z, 2));
        float atten = this.attenuation.x + d * attenuation.y + d * d * attenuation.z;
        return new Color(color.r / atten, color.g / atten, color.b / atten, 1f);
    }

    @Override
    public float GetRay(@Point3d Vector3 point, Ray ray) {
        float t;
        ray.origin = point;
        
        ray.direction = Vector3.sub(pos, point);
        t = Vector3.Norm(ray.direction);
        ray.direction = Vector3.Normalize(ray.direction);

        return t;
    }

    public void fillMissing() { }

    public Vector3 attenuation;
    public @Point3d Vector3 pos;
}
