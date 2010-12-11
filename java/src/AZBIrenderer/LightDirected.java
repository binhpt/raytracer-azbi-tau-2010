/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package AZBIrenderer;

/**
 *
 * @author adam
 */
public class LightDirected extends Light{

    public Color EffectFromLight(Point3 p)
    {
        return this.color;
    }

    //OPTIMIZE
    public float GetRay(Point3 point, Ray ray)
    {
        ray.origin = point;
        ray.direction = reverseDirection;
        return Float.POSITIVE_INFINITY;
    }
    
    public Vector3 direction;
    public Vector3 reverseDirection; //for efficiency

    public void fillMissing()
    {
        direction = Vector3.Normalize(direction);
        reverseDirection = new Vector3(-direction.x, -direction.y, -direction.z);
    }
}
