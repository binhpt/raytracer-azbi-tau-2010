
package AZBIrenderer;

/**
 * An interface for getting the "material" of a geometric surface
 * @author Barak Itkin
 */
public interface SurfaceMat {

    Color getCheckers_diffuse1();

    Color getCheckers_diffuse2();

    float getCheckers_size();

    Color getMtl_ambient();

    Color getMtl_diffuse();

    int getMtl_shininess();

    Color getMtl_specular();

    String getMtl_type();

    int getReflectence();

    String getTexture();

    void setCheckers_diffuse1(Color checkers_diffuse1);

    void setCheckers_diffuse2(Color checkers_diffuse2);

    void setCheckers_size(float checkers_size);

    void setMtl_ambient(Color mtl_ambient);

    void setMtl_diffuse(Color mtl_diffuse);

    void setMtl_shininess(int mtl_shininess);

    void setMtl_specular(Color mtl_specular);

    void setMtl_type(String mtl_type);

    void setReflectence(int reflectence);

    void setTexture(String texture);

}
