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
        return this.intensity;
    }

    //OPTIMIZE
    public Ray GetRay(Point3 point)
    {
        Ray ray = new Ray(point, new Vector3(- direction.x, - direction.y, - direction.z));
        return ray;
    }
    
    public Vector3 direction;

    public void fillMissing() { }
}
