
package AZBIrenderer;
import java.util.LinkedList;

/**
 * Hemispherical lighting.
 * This class recieves a special treatment in the Render core, so it does not
 * implement exactly the Light interface
 * @author Adam Zeira
 */
public class LightHemisphere extends Light{

    LightHemisphere()
    {
        super();
        color_ground = new Color(0, 0, 0, 1);
        up_direction = new Vector3(0, 1, 0);
    }
    public Color color_ground;
    public Vector3 up_direction;

    /*
     * unlike the other lights, LightHemisphere receives the normal
     * instead of the ordinary point
     * since we have to write special code for it anyway
     */
    @Override
    public Color EffectFromLight(Vector3 normal) {
        Color c = new Color(0, 0, 0, 1);
        float air, ground;
        air = (float)Vector3.InnerProduct(normal, up_direction);
        //if (air < 0) air = -air;
        //air++;

        if (air < 0)
        {
            ground = -air;
            ground++;
            ground /= 2;
            air = 1 - ground;
        }
        else
        {
            air++;
            air /= 2;
            ground = 1 - air;
        }

        //ground = (float)Vector3.InnerProduct(Vector3.mul(-1, normal), up_direction);
        c.r = air * color.r + ground * color_ground.r;
        c.g = air * color.g + ground * color_ground.g;
        c.b = air * color.b + ground * color_ground.b;
        
        return c;
    }

    @Override
    public double GetRay(Vector3 point, LinkedList<Ray> lightrays) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void fillMissing() {
    }
}
