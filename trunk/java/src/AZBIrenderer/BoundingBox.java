package AZBIrenderer;

/**
 * A class for representing a 3D box, whose edges are parrallel to the X Y and Z
 * axises. This is going to be used later for some optimizations (such as octal
 * trees or other ideas we may implement).
 * @author Adam Zeira & Barak Itkin
 */
public class BoundingBox {

    /**
     * One corner of the bounding box
     */
    Vector3 p1;
    /**
     * The other corner of the bounding box
     */
    Vector3 p2;

    /**
     * Construct the box by specifying the two opposite corners
     */
    public BoundingBox(Vector3 p1, Vector3 p2) {
        this.p1 = p1;
        this.p2 = p2;
    }

    /**
     * Construct a bounding box which contains all the given bounding boxes
     * @param boxes An array of boxes - <b>it's length must not be zero!</b>
     * @return A box containing all the given bounding boxes
     */
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

    /**
     * Construct a bounding box which contains all the given points
     * @param boxes An array of points - <b>it's length must not be zero!</b>
     * @return A box containing all the given points
     */
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
