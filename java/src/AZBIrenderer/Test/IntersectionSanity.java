/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package AZBIrenderer.Test;

import AZBIrenderer.Debug;
import AZBIrenderer.IntersectionData;
import AZBIrenderer.Point3;
import AZBIrenderer.Ray;
import AZBIrenderer.Sphere;
import AZBIrenderer.Vector3;

/**
 *
 * @author user
 */
public class IntersectionSanity {

    public static void main(String[] args) {
        Debug.DEBUG = true;
        
        IntersectionData d = new IntersectionData();
        /* Sphere + Ray */
        Ray r = new Ray(new Point3(), new Vector3(0, 0, 1));
        Sphere s = new Sphere();
        s.center = new Point3(0, 0, 1);
        s.radius = 0.2f;

        s.Intersection(r, d);
        System.out.println(Debug.makeString(d.point));
    }

}
