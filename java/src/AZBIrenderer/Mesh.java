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

    public static class Triangle extends Face {

        public Point3 A, B, C, p3;
        public Vector3 v0, v1;
        public float dot00, dot01, dot11, invDenom;
        public Vector3 normal;
        public float d;

        public Triangle(SingleMaterialSurface sf,
                Point3 A, Point3 B, Point3 C) {
            super(sf);

            this.A = A;
            this.B = B;
            this.C = C;

            this.v0 = sub(C, A);
            this.v1 = sub(B, A);

            this.dot00 = InnerProduct(this.v0, this.v0);
            this.dot01 = InnerProduct(this.v0, this.v1);
            this.dot11 = InnerProduct(this.v1, this.v1);

            this.normal = Normalize(CrossProduct(v0, v1));
            this.d = -InnerProduct(this.normal, this.A);

            this.invDenom = 1 / (dot00 * dot01 - dot11 * dot11);
        }

        public AZBIrenderer.BoundingBox BoundingBox() {
            return BoundingBox.create(A, B, C);
        }

        // TODO: FIXME!
        // TODO: Fix the pdf - it's v and not minus v
        public boolean Intersection(Ray r, IntersectionData intersect) {
            if (!Math3D.RayPlanintersection(r, normal, d, intersect)) {
                return false;
            }
            Point3 P = intersect.point;

            Vector3 v2 = sub(P, A);

            // Compute dot products
            float dot02 = InnerProduct(v0, v2);
            float dot12 = InnerProduct(v1, v2);

            // Compute barycentric coordinates
            invDenom = 1 / (dot00 * dot11 - dot01 * dot01);
            float u = (dot11 * dot02 - dot01 * dot12) * invDenom;
            float v = (dot00 * dot12 - dot01 * dot02) * invDenom;

            // Check if point is in triangle
            if (u < 0 || v < 0 || u + v > 1)
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
