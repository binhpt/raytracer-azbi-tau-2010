
package AZBIrenderer;

/**
 *
 */
public abstract class Surface {

    public abstract boolean Intersection (final Ray r, IntersectionData intersect);
    public abstract BoundingBox BoundingBox ();

    protected String mtl_type;
    protected Color mtl_diffuse;
    protected Color mtl_specular;
    protected Color mtl_ambient;
    protected int mtl_shininess;
    protected int checkers_size;
    protected Color checkers_diffuse1;
    protected Color checkers_diffuse2;
    protected String texture;
    protected int reflectence;
}