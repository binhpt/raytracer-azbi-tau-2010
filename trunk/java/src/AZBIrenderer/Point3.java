/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package AZBIrenderer;

/**
 * This class is needed for reflection purposes, to seperate points (which
 * shouldn't be normalized) from vectors (which should be normalized)
 * @author Barak Itkin
 */
public class Point3 extends Vector3 {

    public Point3(Vector3 v) {
        super(v);
    }

    public Point3() {
        super();
    }

    public Point3(float x, float y, float z) {
        super(x, y, z);
    }

    public Point3(float[] c) {
        super();
    }

}
