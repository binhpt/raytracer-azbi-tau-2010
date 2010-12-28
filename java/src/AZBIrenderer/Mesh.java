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
        public @Point3d Vector3[] vertices;

        public RawMesh(int verticeCount, int faceCount) {
            this.vertices = new Vector3[verticeCount];
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
        this.pos = new Vector3();
        this.shader = Shader.PHONG;
    }


    /**
     * A class for representing triangular faces.
     */
    public static class Triangle extends Face {

        public @Point3d Vector3 A, B, C, p3;
        /**
         * sub(C, A)
         */
        public Vector3 v0;
        /**
         * sub(B, A)
         */
        public Vector3 v1;
        /**
         * A vector on the plane, orthogonal to v0 - so that we will treat v0 as
         * a Y axis, and v0_N as a X axis
         */
        public Vector3 v0_N;
        public double dot00, dot01, dot11, invDenom;
        public Vector3 normal, nA, nB, nC;
        public double d;
        public boolean flat;

        public Triangle(SingleMaterialSurface sf, boolean flat,
                @Point3d Vector3 A, @Point3d Vector3 B, @Point3d Vector3 C) {
            super(sf);

            this.flat = flat;

            this.A = A;
            this.B = B;
            this.C = C;

            this.v0 = sub(this.C, this.A);
            this.v1 = sub(this.B, this.A);

            this.normal = Normalize(CrossProduct(v0, v1));

            this.dot00 = InnerProduct(this.v0, this.v0);
            this.dot01 = InnerProduct(this.v0, this.v1);
            this.dot11 = InnerProduct(this.v1, this.v1);

            this.d = -InnerProduct(this.normal, this.A);

            this.invDenom = 1 / (dot00 * dot01 - dot11 * dot11);
            if (dot01 != 0)
                this.v0_N = Normalize(CrossProduct(this.v0, this.normal));
            else
                this.v0_N = v1;
        }

        // Must be called if phong is used!
        public void optimize() {
            if (InnerProduct(v1, v0) == 0) // This will cause havoc in Phong! So make sure it doesn't happen!
                // It's important to calculate the normal before this change!
            {
                Vector3 temp = this.A;
                this.A = B;
                this.B = C;
                this.C = temp;

                this.v0 = sub(this.C, this.A);
                this.v1 = sub(this.B, this.A);

                this.normal = Normalize(CrossProduct(v0, v1));

                this.dot00 = InnerProduct(this.v0, this.v0);
                this.dot01 = InnerProduct(this.v0, this.v1);
                this.dot11 = InnerProduct(this.v1, this.v1);

                this.d = -InnerProduct(this.normal, this.A);

                this.invDenom = 1 / (dot00 * dot01 - dot11 * dot11);
                if (dot01 != 0)
                    this.v0_N = Normalize(CrossProduct(this.v0, this.normal));
                else
                    this.v0_N = v1;

                temp = this.nA;
                this.nA = this.nB;
                this.nB = this.nC;
                this.nC = temp;
            }

        }

        public AZBIrenderer.BoundingBox BoundingBox() {
            return BoundingBox.create(A, B, C);
        }

        /* It's true that we are not supposed to implement u and v coordinates
         * for triangular meshes, but since we already do it as a part of the
         * calculation, then we might as well store them here for the future if
         * for some reason we'll support UV unwrapping...
         */
        public boolean Intersection(Ray r, IntersectionData intersect, boolean doUV) {
            if (!Math3D.RayPlanintersection(r, normal, d, intersect)) {
                return false;
            }
            @Point3d Vector3 P = intersect.point;

            Vector3 v2 = sub(P, A);

            // Compute dot products
            double dot02 = InnerProduct(v0, v2);
            double dot12 = InnerProduct(v1, v2);

            // Compute barycentric coordinates
            invDenom = 1 / (dot00 * dot11 - dot01 * dot01);
            // Multiplier of A->C (v0)
            intersect.u = (dot11 * dot02 - dot01 * dot12) * invDenom;
            // Multiplier of A->B (v1)
            intersect.v = (dot00 * dot12 - dot01 * dot02) * invDenom;

            // Check if point is in triangle
            if (intersect.u < 0 || intersect.v < 0 || intersect.u + intersect.v > 1)
                return false;

            if (flat) {
                intersect.normal = normal;
            } else {
                /*
                 *   C                Y-Axis: v0
                 *   *                X-Axis: v0_N
                 *   |\
                 *   | \              v0 = C-A
                 *   |  \             v1 = B-A
                 *   | P \            v2 = P-A
                 * tC*-*--*---*tB
                 *   |c    \ /        yB = InnerProduct(v1, this.v0) ==> dot01
                 *   |      *         yC = InnerProduct(v0, this.v0) ==> dot00
                 *   |     / B
                 *  a|    /b
                 *   |   /
                 *   |  /
                 *   | /
                 *   |/
                 *   *----------->v0_N
                 *   A
                 */

                // DO NOT DO betta = dot02/dot01, since if dot01=0 it's a problem
                double alpha = dot02/dot00, betta = dot02/dot01;
                Vector3 lB = add(mul (betta, nB), mul(1- betta, nA));
                Vector3 lC = add(mul (alpha, nC), mul(1- alpha, nA));
                double phi = InnerProduct(v2, v0_N) / (betta * InnerProduct(v1, v0_N));
                intersect.normal = add(mul(phi, lB), mul(1-phi, lC));
            }
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
    public @Point3d Vector3 pos;
    /**
     * The scaling to apply to the mesh
     */
    public double scale;
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
        @Point3d Vector3 center = mul(1 / 2, add(temp.max, temp.min));
        boolean flat = (this.shader == Shader.FLAT);

        for (int i = 0; i < mesh.vertices.length; i++) {
            mesh.vertices[i] = add(pos, add(mul(scale, sub(mesh.vertices[i], center)), center));
        }

        int[] face;
        for (int i = 0; i < mesh.faces.length; i++) {
            face = mesh.faces[i];
            this.triangles[i] = new Triangle(this, flat, mesh.vertices[face[0]], mesh.vertices[face[1]], mesh.vertices[face[2]]);
        }

        if (this.shader == Shader.PHONG) {
            Vector3[] vNormals = new Vector3[mesh.vertices.length];
            // Init the vertice normals to (0,0,0)
            for (int i = 0; i < vNormals.length; i++) {
                vNormals[i] = new Vector3();
            }
            // For each face
            for (int i = 0; i < mesh.faces.length; i++) {
                face = mesh.faces[i];
                // For each vertice of the face, add the face normal to the
                // vertice normal
                for (int vert : face) {
                    vNormals[vert] = add(vNormals[vert], this.triangles[i].normal);
                }
            }
            // Normalize the vertice normals
            for (int i = 0; i < vNormals.length; i++) {
                vNormals[i] = Normalize(vNormals[i]);
                if (InnerProduct(vNormals[i], vNormals[i]) == 0)
                    System.err.println("Bad Normal");
            }
            // For each face, assign the vertice normal to it's vertices
            for (int i = 0; i < mesh.faces.length; i++) {
                face = mesh.faces[i];
                Triangle triangle = this.triangles[i];

                triangle.nA = vNormals[face[0]];
                triangle.nB = vNormals[face[1]];
                triangle.nC = vNormals[face[2]];
            }
        }

    }

    public Object[] getRealObjects() {
        return triangles;
    }

    @Override
    public void fillMissing() {
        initFromRawMesh(MeshParser.parse(filename));
        for (Triangle face : triangles) {
            face.optimize();
        }
    }
}
