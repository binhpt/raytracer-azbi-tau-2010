
package AZBIrenderer;

import java.awt.image.BufferedImage;

/**
 * An interface for getting the "material" of a geometric surface
 * @author Barak Itkin
 */
public interface SurfaceMat {

    Color getCheckers_diffuse1();

    Color getCheckers_diffuse2();

    double getCheckers_size();

    Color getMtl_ambient();

    Color getMtl_diffuse();

    int getMtl_shininess();

    Color getMtl_specular();

    String getMtl_type();

    double getReflectance();

    BufferedImage getTexture();

    void setCheckers_diffuse1(Color checkers_diffuse1);

    void setCheckers_diffuse2(Color checkers_diffuse2);

    void setCheckers_size(double checkers_size);

    void setMtl_ambient(Color mtl_ambient);

    void setMtl_diffuse(Color mtl_diffuse);

    void setMtl_shininess(int mtl_shininess);

    void setMtl_specular(Color mtl_specular);

    void setMtl_type(String mtl_type);

    void setReflectance(double reflectance);

    void setTexture(BufferedImage texture);

    public Color GetDiffuse2(double u, double v);
}
