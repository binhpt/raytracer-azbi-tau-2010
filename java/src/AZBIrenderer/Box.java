package AZBIrenderer;

import java.lang.reflect.Field;
import static AZBIrenderer.Vector3.*;
import java.util.List;
/**
 * A class for representing 3D paralleloids in 3D space
 * <pre>
 *      p6
 *      X----------X
 *     /|         /|
 *    / |        / |
 * p3/  |     p5/  |
 *  X----------X   |
 *  |   |      |   |
 *  |   p2     |   |
 *  |w  X------|---X
 *  |  /       |  /p4
 *  | /v       | /
 *  |/    u    |/
 *  X----------X
 * p0         p1
 * </pre>
 * @author Adam Zeira & Barak Itkin
 */
public class Box extends Surface {

    enum Side
    {
        TOP(0),
        BOTTOM(1),
        LEFT(2),
        RIGHT(3),
        NEAR(4),
        FAR(5);

        int i;
        Side(int index)
        {
            this.i = index;
        }
    }

    public Point3 p0;
    public Point3 p1;
    public Point3 p2;
    public Point3 p3;

    /**
     * The vector from p0 to p1
     */
    public Vector3 u;
    /**
     * The vector from p0 to p2
     */
    public Vector3 v;
    /**
     * The vector from p0 to p3
     */
    public Vector3 w;
    /**
     * The surface's normal
     */
    public Vector3 normal;
    /**
     * The variable d in the plain equation:
     * <pre>Ax + By + Cz + d = 0</pre>
     */
    public float d;
    /**
     * A value cached for performance reasons
     * <pre>Innerproduct(u,u)</pre>
     */
    public float unormSquare;
    /**
     * A value cached for performance reasons
     * <pre>Innerproduct(v,v)</pre>
     */
    public float vnormSquare;
    /**
     * A value cached for performance reasons
     * <pre>Innerproduct(w,w)</pre>
     */
    public float wnormSquare;

    public Rectangle[] faces;
    public List<Rectangle> faceList;
    boolean OK = true;

    @Override
    public boolean Intersection(Ray r, IntersectionData intersect) {
        return Render.shootAtSurfaces(faceList, r, intersect);
    }

    @Override
    public AZBIrenderer.BoundingBox BoundingBox() {
        BoundingBox top = faces[Side.TOP.i].BoundingBox();
        BoundingBox bottom = faces[Side.TOP.i].BoundingBox();
        return new BoundingBox(
                CoordinateMax(top.p1, CoordinateMax(top.p2, CoordinateMax(bottom.p1, bottom.p2))),
                CoordinateMin(top.p1, CoordinateMin(top.p2, CoordinateMin(bottom.p1, bottom.p2)))
                );
    }

    @Override
    public void fillMissing() {
        // Left
        u = sub(p1, p0);
        // In
        v = sub(p2, p0);
        // Up
        w = sub(p3, p0);

        Point3 p4 = new Point3(add(p1,v));
        Point3 p5 = new Point3(add(p1,w));
        Point3 p6 = new Point3(add(p2,w));

        faces = new Rectangle[6];

        faces[Side.BOTTOM.i] = new Rectangle(p0, p1, p2);
        faces[Side.TOP.i] = new Rectangle(p3, p5, p6);
        faces[Side.NEAR.i] = new Rectangle(p0, p1, p3);
        faces[Side.FAR.i] = new Rectangle(p2, p4, p6);
        faces[Side.LEFT.i] = new Rectangle(p0, p2, p3);
        faces[Side.RIGHT.i] = new Rectangle(p1, p4, p5);

        for (Rectangle face : faces) {
            for (Field field : Surface.class.getFields()) {
                try {
                    field.set(face, field.get(this));
                } catch (Exception ex) {
                    System.err.println("Something is really wrong!"
                            + "I can't initialize the rectangles from the box...");
                    OK = false;
                }
            }
            face.fillMissing();
        }
        faceList = java.util.Arrays.asList(faces);
    }

}
