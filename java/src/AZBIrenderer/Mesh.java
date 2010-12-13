package AZBIrenderer;

import static AZBIrenderer.Vector3.*;

/**
 * A class for representing triangular meshes, which is actually a wrapper
 * around Many {@link Triangle} faces.
 * @author Barak Itkin
 * @see Triangle
 */
public class Mesh extends SingleMaterialSurface implements ReflectionConstructed, ReflectionWrapper {

    /**
     * Represent a raw mesh, as a list of points and faces
     */
    public static class RawMesh {

        public int[][] faces;
        public Point3[] vertices;

        public RawMesh(int verticeCount, int faceCount) {
            this.vertices = new Point3[verticeCount];
            this.faces = new int[faceCount][3];
        }
    }

    /**
     * The shading types for a mesh object
     */
    public static enum Shader {

        FLAT, PHONG;
    }

    public Mesh() {
        this.scale = 1;
        this.pos = new Point3();
    }


    /**
     * A class for representing triangular faces.
     */
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

    /**
     * The file from which the mesh should be loaded
     */
    public String filename;
    /**
     * The offset for the mesh
     */
    public Point3 pos;
    /**
     * The scaling to apply to the mesh
     */
    public float scale;
    /**
     * The shading type for this mesh
     */
    public Shader shader;
    /**
     * The actual triangular faces of this mesh
     */
    public Triangle[] triangles;

    private void initFromRawMesh(RawMesh mesh) {
        if (mesh == null) {
            this.triangles = new Triangle[0];
            return;
        }

        this.triangles = new Triangle[mesh.faces.length];

        BoundingBox temp = BoundingBox.create(mesh.vertices);
        Vector3 center = mul(1/2, add (temp.p1, temp.p2));

        for (int i = 0; i < mesh.vertices.length; i++) {
            mesh.vertices[i] = new Point3(add(mul(scale,sub(mesh.vertices[i], center)), center));
        }
        
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
