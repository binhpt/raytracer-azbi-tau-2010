/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package AZBIrenderer.Test;

import AZBIrenderer.IntersectionData;
import AZBIrenderer.Mesh.Triangle;
import AZBIrenderer.Point3;
import AZBIrenderer.Ray;
import AZBIrenderer.Rectangle;
import AZBIrenderer.Vector3;

/**
 *
 * @author user
 */
public class IntersectionSanity {

    public static void main(String[] args) {
        Triangle m = new Triangle(null, new Point3(0,0,0), new Point3(1,1,0), new Point3(2,0,0));
        Ray r = new Ray(new Point3(1.5f, 0.25f, 0f), new Vector3(0, 0, 1));
        IntersectionData d = new IntersectionData();
        System.out.println(m.Intersection(r, d));
    }

}
