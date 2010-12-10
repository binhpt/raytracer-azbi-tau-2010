package AZBIrenderer;

/**
 * A base class for 3D surfaces
 * @author Adam Zeira & Barak Itkin
 */
public abstract class Surface implements ReflectionConstructed {

    /**
     * Compue the first intersection of a ray with this surface
     * @param r The ray to intersect with
     * @param intersect A location for storing the intersection data in
     * @return Whether there is an intersection
     */
    public abstract boolean Intersection(final Ray r, IntersectionData intersect);

    /**
     * Compute the bounding box containing this object
     * @return A bounding box containing this object
     */
    public abstract BoundingBox BoundingBox();

    public Surface() {
        this.mtl_type = "flat";
        this.mtl_diffuse = new Color(0.7f, 0.7f, 0.7f, 1);
        this.mtl_specular = new Color(1, 1, 1, 1);
        this.mtl_ambient = new Color(0.1f, 0.1f, 0.1f, 1);
        this.mtl_shininess = 100;
        this.checkers_size = 0.1f;
        this.checkers_diffuse1 = new Color(1, 1, 1, 1);
        this.checkers_diffuse2 = new Color(0.2f, 0.2f, 0.2f, 1);
        this.texture = null;
        this.reflectence = 0;
    }
    public String mtl_type;
    public Color mtl_diffuse;
    public Color mtl_specular;
    public Color mtl_ambient;
    public int mtl_shininess;
    public float checkers_size;
    public Color checkers_diffuse1;
    public Color checkers_diffuse2;
    public String texture;
    public int reflectence;
}
