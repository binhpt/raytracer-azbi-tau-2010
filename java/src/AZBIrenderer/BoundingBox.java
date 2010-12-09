
package AZBIrenderer;

/**
 * A class for representing a 3D box, whose edges are parrallel to the X Y and Z
 * axises.
 */
public class BoundingBox {

    Vector3 p1;
    Vector3 p2;

    /**
     * Construct the box by specifying the two opposite corners
     */
    public BoundingBox(Vector3 p1, Vector3 p2) {
        this.p1 = p1;
        this.p2 = p2;
    }

}
