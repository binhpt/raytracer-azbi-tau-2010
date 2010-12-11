/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package AZBIrenderer;

/**
 *
 * @author user
 */
public interface SurfaceI {

    /**
     * Compue the first intersection of a ray with this surface
     * @param r The ray to intersect with
     * @param intersect A location for storing the intersection data in
     * @return Whether there is an intersection
     */
    public boolean Intersection(final Ray r, IntersectionData intersect);

    /**
     * Compute the bounding box containing this object
     * @return A bounding box containing this object
     */
    public BoundingBox BoundingBox();

    public Color getCheckers_diffuse1();

    public void setCheckers_diffuse1(Color checkers_diffuse1);

    public Color getCheckers_diffuse2();

    public void setCheckers_diffuse2(Color checkers_diffuse2);

    public float getCheckers_size();

    public void setCheckers_size(float checkers_size);

    public Color getMtl_ambient();

    public void setMtl_ambient(Color mtl_ambient);

    public Color getMtl_diffuse();

    public void setMtl_diffuse(Color mtl_diffuse);

    public int getMtl_shininess();

    public void setMtl_shininess(int mtl_shininess);

    public Color getMtl_specular();

    public void setMtl_specular(Color mtl_specular);

    public String getMtl_type();

    public void setMtl_type(String mtl_type);

    public int getReflectence();

    public void setReflectence(int reflectence);

    public String getTexture();

    public void setTexture(String texture);
}
