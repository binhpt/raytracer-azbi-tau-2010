package AZBIrenderer;

import java.util.ArrayList;
import static AZBIrenderer.Vector3.*;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * A highly optimized lazy octal tree.
 * <ol>
 *  <li>Feed in all the objects.</li>
 *  <li>Call the subdivide method. Once called, it mustn't be called again, and
 *      no objects should be added.</li>
 *  <li>Ask for the objects relevant to the given ray</li>
 * </ol>
 *
 * The optimization of the octal tree goes like this:
 * <ul>
 *  <li>A box node with no children will be deleted!</li>
 *  <li>A box node with a single child, will be replaced by it's single child</li>
 * </ul>
 * @author Barak Itkin
 */
public final class OctalTree {

    public BoxNode root;

    public enum OptimizeHow {
        I_AM_EMPTY, I_HAVE_FEW_SURFACES, I_HAVE_ONE_CHILD, NOTHING;
    }

    public final class BoxNode extends BoundingBox {
        
        public List<Surface> content;
        public List<BoxNode> children;
        public BoxNode parent;
        public int depth;

        public BoxNode(Vector3 p1, Vector3 p2, int depth) {
            super(p1, p2);
            this.depth = depth;
        }

        private void doSubdivision() {
            if (!isLeaf()) {
                this.children = new LinkedList<BoxNode>();
                @Point3d Vector3 center = mul(0.5f, add(this.max, this.min));
                double[][] c = {{max.x, max.y, max.z}, {min.x, min.y, min.z}};
                for (int i = 0; i < 8; i++) {
                    BoxNode child = new BoxNode(center, new Vector3(c[i / 4 % 2][0], c[i / 2 % 2][1], c[i % 2][2]), depth - 1);
                    child.parent = this;
                    children.add(child);
                }
            } else {
                this.content = new LinkedList<Surface>();
            }
        }

        public boolean isLeaf() {
            return this.depth == 0;
        }

        public void addObject(BoundingBox bounds, Surface obj) {
            if (!this.intersects(bounds))
                return;

            if (isLeaf()) {
                if (this.content == null)
                    this.doSubdivision();
                this.content.add(obj);
            } else {
                if (this.children == null)
                    this.doSubdivision();
                for (BoxNode child : this.children) {
                    child.addObject(bounds, obj);
                }
            }
        }

        public OptimizeHow optimize () {
            if (this.children != null && this.children.isEmpty())
                this.children =null;

            if (this.content != null && this.content.isEmpty())
                this.content = null;

            if (this.children != null) {
                int count = this.children.size();
                BoxNode child;
                for (int i = 0, j = 0; i < count; i++) {
                    switch ((child = this.children.get(i-j)).optimize()) {
                        case I_AM_EMPTY:
                            this.children.remove(child);
                            j++;
                            break;
                        case I_HAVE_ONE_CHILD:
                            BoxNode grandChild = child.children.get(0);
                            // Child will copy the bounds from the grandchild
                            child.copyFrom(grandChild);
                            // Child will copy the content from the grandchild
                            child.content = grandChild.content;
                            // Child will have no children
                            child.children = null;
                        case I_HAVE_FEW_SURFACES:
                            if (this.content == null)
                                this.content = child.content;
                            else
                                this.content.addAll(child.content);
                            children.remove(child);
                            j++;
                            break;
                    }
                }
            }

            if (this.children != null && this.children.isEmpty())
                this.children =null;

            if (this.content != null && this.content.isEmpty())
                this.content = null;

            // If I have no children, and I have no content, then I'm useless!
            if (this.children == null && this.content == null) {
                return OptimizeHow.I_AM_EMPTY;
            }

            if (this.content != null && children == null && this.content.size() <= 6)
                return OptimizeHow.I_HAVE_FEW_SURFACES;

            if (this.children != null && children.size() == 1)
                return OptimizeHow.I_HAVE_ONE_CHILD;

            return OptimizeHow.NOTHING;
        }

        public void getObjects(Ray r, List<Surface> dest) {
            if (OctalTree.this.root != this && !this.intersects(r))
                return;


            if (this.content != null)
                dest.addAll(this.content);
            if (this.children != null) {
                for (BoxNode child : this.children) {
                    child.getObjects(r, dest);
                }
            }
        }

//        @Override
//        public boolean intersects(Ray r) {
//            return Render.ShootLightAtSurfaces(faces, r, Float.MAX_VALUE, null);
//        }


    }

    public OctalTree(List<Surface> geometry, int depth) {
        BoundingBox[] boxes = new BoundingBox[geometry.size()];
        int i = 0;
        for (Surface surf : geometry) {
            boxes[i++] = surf.BoundingBox();
        }
        BoundingBox outter = BoundingBox.create(boxes);
        this.root = new BoxNode(outter.min, outter.max, depth);
        i = 0;
        for (Surface surf : geometry) {
            this.root.addObject(boxes[i++], surf);
        }
        this.root.optimize();
        this.printRecursive(root, "");
    }

    public void getObjects(Ray r, List<Surface> dest) {
        root.getObjects(r, dest);
    }

    public void printRecursive(BoxNode b, String indent) {
        if (b == null) return;
        System.out.println(indent + b);
        if (!b.isLeaf())
            for (BoxNode child : b.children) {
                printRecursive(child, indent+"  ");
            }
    }


}
