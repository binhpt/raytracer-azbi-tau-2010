
package AZBIrenderer;

/**
 *
 */
public class Color {

    float r, g, b, a;

    public Color() {
    }

    public Color(float r, float g, float b, float a) {
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
    }

    public void fillArray (float[] array)
    {
        array[0] = r;
        array[1] = g;
        array[2] = b;
        array[3] = a;
    }

}
