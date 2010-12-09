
package AZBIrenderer;

/**
 *
 */
public abstract class Surface {

    public abstract boolean Intersection (final Ray r, IntersectionData intersect);
    public abstract BoundingBox BoundingBox ();

    public String mtl_type;
    public Color mtl_diffuse;
    public Color mtl_specular;
    public Color mtl_ambient;
    public int mtl_shininess;
    public int checkers_size;
    public Color checkers_diffuse1;
    public Color checkers_diffuse2;
    public String texture;
    public int reflectence;
}