package AZBIrenderer;

import static AZBIrenderer.Vector3.*;
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
 *  |   X------|---X
 *  |  /       |  /p4
 *  | /        | /
 *  |/         |/
 *  X----------X
 * p0         p1
 * </pre>
 * @author Adam Zeira & Barak Itkin
 */
public class Box extends SingleMaterialSurface implements ReflectionConstructed, ReflectionWrapper {

    public Point3 p0;
    public Point3 p1;
    public Point3 p2;
    public Point3 p3;

    public Rectangle.RectangleFace[] real;

    public Box() { }

    public Object[] getRealObjects() {
        return real;
    }

    @Override
    public void fillMissing() {
        Vector3 u = sub(p1,p0);
        Vector3 v = sub(p2,p0);
        Vector3 w = sub(p3,p0);

        Point3 p4 = new Point3(add(p1,v));
        Point3 p5 = new Point3(add(p1,w));
        Point3 p6 = new Point3(add(p2,w));

        this.real = new Rectangle.RectangleFace[6];
        SingleMaterialSurface oneMaterial = this.makeCopyOfMaterial();

        this.real[0] = new Rectangle.RectangleFace(oneMaterial, p0, p1, p2);
        this.real[1] = new Rectangle.RectangleFace(oneMaterial, p0, p1, p3);
        this.real[2] = new Rectangle.RectangleFace(oneMaterial, p0, p2, p3);

        this.real[3] = new Rectangle.RectangleFace(oneMaterial, p1, p4, p5);
        this.real[4] = new Rectangle.RectangleFace(oneMaterial, p3, p5, p6);
        this.real[5] = new Rectangle.RectangleFace(oneMaterial, p2, p4, p6);
    }


}
