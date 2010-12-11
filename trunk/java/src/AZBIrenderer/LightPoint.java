/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package AZBIrenderer;

/**
 *
 * @author adam
 */
public class LightPoint extends Light{
    public LightPoint()
    {
        super();
        attenuation = new Vector3(0, 1, 0);
    }

    @Override
    public Color EffectFromLight(Point3 point) {
        float d = Vector3.InnerProduct(point, this.pos);
        float atten = this.attenuation.x + d * attenuation.y + d * d * attenuation.z;
        return new Color(color.r / atten, color.g / atten, color.b / atten, 1f);
    }

    @Override
    public float GetRay(Point3 point, Ray ray) {
        float t;
        ray.origin = point;
        ray.direction = Vector3.sub(pos, point);
        t = Vector3.Norm(ray.direction);
        ray.direction.x /= t;
        ray.direction.y /= t;
        ray.direction.z /= t;

        return t;
    }

    public void fillMissing() { }

    public Vector3 attenuation;
    public Point3 pos;
}