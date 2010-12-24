
package AZBIrenderer;

import java.awt.image.BufferedImage;

/**
 * A delegate class around SingleMaterialSurface for many objects which share a
 * single material - or in simple English, Faces.
 * Note that childs of this class should be actual geometric surfaces (this is
 * why this class "implements" the {@link Surface} interface.
 * @author Barak Itkin
 */
public abstract class Face implements Surface {

    public SingleMaterialSurface surfaceMaterial;

    public Face(SingleMaterialSurface sf) {
        this.surfaceMaterial = sf;
    }

    @Override
    public void setTexture(BufferedImage texture) {
        surfaceMaterial.setTexture(texture);
    }

    @Override
    public void setReflectance(float reflectance) {
        surfaceMaterial.setReflectance(reflectance);
    }

    @Override
    public void setMtl_type(String mtl_type) {
        surfaceMaterial.setMtl_type(mtl_type);
    }

    @Override
    public void setMtl_specular(Color mtl_specular) {
        surfaceMaterial.setMtl_specular(mtl_specular);
    }

    @Override
    public void setMtl_shininess(int mtl_shininess) {
        surfaceMaterial.setMtl_shininess(mtl_shininess);
    }

    @Override
    public void setMtl_diffuse(Color mtl_diffuse) {
        surfaceMaterial.setMtl_diffuse(mtl_diffuse);
    }

    @Override
    public void setMtl_ambient(Color mtl_ambient) {
        surfaceMaterial.setMtl_ambient(mtl_ambient);
    }

    @Override
    public void setCheckers_size(float checkers_size) {
        surfaceMaterial.setCheckers_size(checkers_size);
    }

    @Override
    public void setCheckers_diffuse2(Color checkers_diffuse2) {
        surfaceMaterial.setCheckers_diffuse2(checkers_diffuse2);
    }

    @Override
    public void setCheckers_diffuse1(Color checkers_diffuse1) {
        surfaceMaterial.setCheckers_diffuse1(checkers_diffuse1);
    }

    @Override
    public BufferedImage getTexture() {
        return surfaceMaterial.getTexture();
    }

    @Override
    public float getReflectance() {
        return surfaceMaterial.getReflectance();
    }

    @Override
    public String getMtl_type() {
        return surfaceMaterial.getMtl_type();
    }

    @Override
    public Color getMtl_specular() {
        return surfaceMaterial.getMtl_specular();
    }

    @Override
    public int getMtl_shininess() {
        return surfaceMaterial.getMtl_shininess();
    }

    @Override
    public Color getMtl_diffuse() {
        return surfaceMaterial.getMtl_diffuse();
    }

    @Override
    public Color getMtl_ambient() {
        return surfaceMaterial.getMtl_ambient();
    }

    @Override
    public float getCheckers_size() {
        return surfaceMaterial.getCheckers_size();
    }

    @Override
    public Color getCheckers_diffuse2() {
        return surfaceMaterial.getCheckers_diffuse2();
    }

    @Override
    public Color getCheckers_diffuse1() {
        return surfaceMaterial.getCheckers_diffuse1();
    }

    public Color GetDiffuse2(float u, float v) {
        return surfaceMaterial.GetDiffuse2(u, v);
    }

    

}
