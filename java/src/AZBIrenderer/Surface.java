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

    public int getReflectence() {
        return reflectence;
    }

    public void setReflectence(int reflectence) {
        this.reflectence = reflectence;
    }

    public String getTexture() {
        return texture;
    }

    public void setTexture(String texture) {
        this.texture = texture;
    }


}
