
package AZBIrenderer;

/**
 * A class for representing RGBA colors in flating point precision. The values
 * of each channel should be inside the range of 0-1
 * @author Adam Zeira & Barak Itkin
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

    public void fillRGBA (float[] array)
    {
        array[0] = r;
        array[1] = g;
        array[2] = b;
        array[3] = a;
    }

    public void fillARGB (float[] array)
    {
        array[0] = a;
        array[1] = r;
        array[2] = g;
        array[3] = b;
    }

    /**
     * Convert the color to javas int RGB format
     * @return The integer representing this color
     */
    public int getRGB()
    {

        return ((int)(a * 255) << 24)
                | ((int)(r * 255) << 16)
                | ((int)(g * 255) << 8)
                | ((int)(b * 255));
    }

    public static Color add(Color a, Color b)
    {
        return new Color(a.r + b.r, a.g + b.g, a.b + b.b, a.a + b.a);
    }

}
