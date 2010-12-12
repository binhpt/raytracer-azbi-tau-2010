package AZBIrenderer;

import static AZBIrenderer.Vector3.*;

/**
 *
 * @author Barak Itkin
 */
public class Mesh extends SingleMaterialSurface implements ReflectionConstructed, ReflectionWrapper {

    public static class RawMesh {

        public int[][] faces;
        public Point3[] vertices;

        public RawMesh(int verticeCount, int faceCount) {
            this.vertices = new Point3[verticeCount];
            this.faces = new int[faceCount][3];
        }
    }

    public static enum Shader {

        FLAT, PHONG;
    }

    public class Triangle extends Face {

        public Point3 p0, p1, p2, p3;
        public Vector3 u, v;
        public float uNormSquare, vNormSquare;
        public Vector3 u1, v1;
        public float u1NormSquare, v1NormSquare;
        public Vector3 normal;
        public float d;

        public Triangle(SingleMaterialSurface sf,
                Point3 p0, Point3 p1, Point3 p2) {
            super(sf);

            this.p0 = p0;
            System.out.println("p0 = " + Debug.makeString(p0));
            this.p1 = p1;
            System.out.println("p1 = " + Debug.makeString(p1));
            this.p2 = p2;
            System.out.println("p2 = " + Debug.makeString(p2));

            this.u = sub(p1, p0);
            System.out.println("u = " + Debug.makeString(u));
            this.uNormSquare = InnerProduct(this.u, this.u);
            System.out.println("<u,u> = " + Debug.makeString(uNormSquare));
            this.v = sub(p2, p0);
            System.out.println("v = " + Debug.makeString(v));
            this.vNormSquare = InnerProduct(this.v, this.v);
            System.out.println("<v,v> = " + Debug.makeString(vNormSquare));

            this.p3 = add(add(p0, u), v);
            System.out.println("p3 = " + Debug.makeString(p3));

            this.u1 = sub(p2, p1);
            System.out.println("u1 = " + Debug.makeString(u1));
            this.u1NormSquare = InnerProduct(this.u1, this.u1);
            System.out.println("<u1,u1> = " + Debug.makeString(u1NormSquare));
            this.v1 = sub(p3, p1);
            System.out.println("v1 = " + Debug.makeString(v1));
            this.v1NormSquare = InnerProduct(this.v1, this.v1);
            System.out.println("<v1,v1> = " + Debug.makeString(v1NormSquare));

            this.normal = Normalize(CrossProduct(u, v));
            this.d = -InnerProduct(this.normal, this.p0);
        }

        public AZBIrenderer.BoundingBox BoundingBox() {
            return BoundingBox.create(p0, p1, p2);
        }

        // TODO: FIXME!
        // TODO: Fix the pdf - it's v and not minus v
        public boolean Intersection(Ray r, IntersectionData intersect) {
            if (!Math3D.RayPlanintersection(r, normal, d, intersect)) {
                return false;
            }
            Vector3 x = sub(intersect.point, p0);

            float temp1 = InnerProduct(x, u), temp2 = InnerProduct(x, v);

            if (temp1 < 0 || temp1 > uNormSquare || temp2 < 0 || temp2 > vNormSquare)
                return false;

            x = sub(intersect.point, p1);

            temp1 = InnerProduct(x, u1);
            temp2 = InnerProduct(x, v1);

            if (!(temp1 < 0 || temp1 > u1NormSquare || temp2 < 0 || temp2 > v1NormSquare))
                return false;

            intersect.normal = normal;
            intersect.surface = this.surfaceMaterial;

            return true;
        }
    }
    public String filename;
    public Point3 pos;
    public float scale;
    public Shader shader;
    public Triangle[] triangles;

    private void initFromRawMesh(RawMesh mesh) {
        if (mesh == null) {
            this.triangles = new Triangle[0];
            return;
        }

        this.triangles = new Triangle[mesh.faces.length];

        int[] face;
        for (int i = 0; i < mesh.faces.length; i++) {
            face = mesh.faces[i];
            this.triangles[i] = new Triangle(this, mesh.vertices[face[0]], mesh.vertices[face[1]], mesh.vertices[face[2]]);
        }

    }

    public Object[] getRealObjects() {
        return triangles;
    }

    @Override
    public void fillMissing() {
        initFromRawMesh(MeshParser.parse(filename));
    }
}
