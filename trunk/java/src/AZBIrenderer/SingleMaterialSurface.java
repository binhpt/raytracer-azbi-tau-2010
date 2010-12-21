package AZBIrenderer;

import java.awt.image.BufferedImage;

/**
 * A base class for objects which have their own material.
 *
 * Many objects extend this class, instead of containing an instance of it as a
 * field - and this may seem a bit weird at first. The reason for this is that
 * the reflection based constructor can only initialize fields from an object's
 * class or it's parent classes. So since it's possible to specify material
 * properties directly for surfaces in the config files (instead of having
 * surface definitions and have the objects point to those materials), many
 * objects extend this class.
 *
 * @author Adam Zeira & Barak Itkin
 */
public class SingleMaterialSurface implements ReflectionConstructed, SurfaceMat {

    private SingleMaterialSurface(String mtl_type, Color mtl_diffuse, Color mtl_specular, Color mtl_ambient, int mtl_shininess, float checkers_size, Color checkers_diffuse1, Color checkers_diffuse2, BufferedImage texture, float reflectance) {
        this.mtl_type = mtl_type;
        this.mtl_diffuse = mtl_diffuse;
        this.mtl_specular = mtl_specular;
        this.mtl_ambient = mtl_ambient;
        this.mtl_shininess = mtl_shininess;
        this.checkers_size = checkers_size;
        this.checkers_diffuse1 = checkers_diffuse1;
        this.checkers_diffuse2 = checkers_diffuse2;
        this.texture = texture;
        this.reflectance = reflectance;
    }

    public SingleMaterialSurface makeCopyOfMaterial() {
        return new SingleMaterialSurface(mtl_type, mtl_diffuse, mtl_specular,
                mtl_ambient, mtl_shininess, checkers_size, checkers_diffuse1,
                checkers_diffuse2, texture, reflectance);
    }

    public SingleMaterialSurface() {
        this.mtl_type = "flat";
        this.mtl_diffuse = new Color(0.7f, 0.7f, 0.7f, 1);
        this.mtl_specular = new Color(1, 1, 1, 1);
        this.mtl_ambient = new Color(0.1f, 0.1f, 0.1f, 1);
        this.mtl_shininess = 100;
        this.checkers_size = 0.1f;
        this.checkers_diffuse1 = new Color(1, 1, 1, 1);
        this.checkers_diffuse2 = new Color(0.2f, 0.2f, 0.2f, 1);
        this.texture = null;
        this.reflectance = 0;
    }

    public String mtl_type;
    public Color mtl_diffuse;
    public Color mtl_specular;
    public Color mtl_ambient;
    public int mtl_shininess;
    public float checkers_size;
    public Color checkers_diffuse1;
    public Color checkers_diffuse2;
    public @FileTexture BufferedImage texture;
    public float reflectance;

    /**
     * Override this if necessary
     */
    public void fillMissing() {  }

    public Color getCheckers_diffuse1() {
        return checkers_diffuse1;
}

    public void setCheckers_diffuse1(Color checkers_diffuse1) {
        this.checkers_diffuse1 = checkers_diffuse1;
    }

    public Color getCheckers_diffuse2() {
        return checkers_diffuse2;
    }

    public void setCheckers_diffuse2(Color checkers_diffuse2) {
        this.checkers_diffuse2 = checkers_diffuse2;
    }

    public float getCheckers_size() {
        return checkers_size;
    }

    public void setCheckers_size(float checkers_size) {
        this.checkers_size = checkers_size;
    }

    public Color getMtl_ambient() {
        return mtl_ambient;
    }

    public void setMtl_ambient(Color mtl_ambient) {
        this.mtl_ambient = mtl_ambient;
    }

    public Color getMtl_diffuse() {
        return mtl_diffuse;
    }

    public void setMtl_diffuse(Color mtl_diffuse) {
        this.mtl_diffuse = mtl_diffuse;
    }

    public int getMtl_shininess() {
        return mtl_shininess;
    }

    public void setMtl_shininess(int mtl_shininess) {
        this.mtl_shininess = mtl_shininess;
    }

    public Color getMtl_specular() {
        return mtl_specular;
    }

    public void setMtl_specular(Color mtl_specular) {
        this.mtl_specular = mtl_specular;
    }

    public String getMtl_type() {
        return mtl_type;
    }

    public void setMtl_type(String mtl_type) {
        this.mtl_type = mtl_type;
    }

    public float getReflectance() {
        return reflectance;
    }

    public void setReflectance(float reflectance) {
        this.reflectance = reflectance;
    }

    public BufferedImage getTexture() {
        return texture;
    }

    public void setTexture(BufferedImage texture) {
        this.texture = texture;
    }

    public Color GetDiffuse(Vector3 point)
    {
        return this.mtl_diffuse;
    }
}
