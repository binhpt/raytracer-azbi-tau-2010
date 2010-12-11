package AZBIrenderer;

/**
 * A class for representing a 3D box, whose edges are parrallel to the X Y and Z
 * axises.
 * @author Adam Zeira & Barak Itkin
 */
public class BoundingBox {

    Point3 p1;
    Point3 p2;

    /**
     * Construct the box by specifying the two opposite corners
     */
    public BoundingBox(Vector3 p1, Vector3 p2) {
        this.p1 = new Point3(p1);
        this.p2 = new Point3(p2);
    }

    /* Arg count may not be zero! */
    public static BoundingBox create(BoundingBox... boxes)
    {
        Vector3 max = new Vector3(Float.MIN_VALUE, Float.MIN_VALUE, Float.MIN_VALUE);
        Vector3 min = new Vector3(Float.MAX_VALUE, Float.MAX_VALUE, Float.MAX_VALUE);

        for (BoundingBox box : boxes) {
            min = Vector3.CoordinateMin(min, Vector3.CoordinateMin(box.p1, box.p2));
            max = Vector3.CoordinateMax(max, Vector3.CoordinateMax(box.p1, box.p2));
        }
        return new BoundingBox(min, max);
    }
    /* Arg count may not be zero! */
    public static BoundingBox create(Vector3... pts)
    {
        Vector3 max = new Vector3(Float.MIN_VALUE, Float.MIN_VALUE, Float.MIN_VALUE);
        Vector3 min = new Vector3(Float.MAX_VALUE, Float.MAX_VALUE, Float.MAX_VALUE);

        for (Vector3 pt : pts) {
            min = Vector3.CoordinateMin(min, pt);
            max = Vector3.CoordinateMax(max, pt);
        }
        return new BoundingBox(min, max);
    }
}
