package AZBIrenderer;

import java.lang.reflect.Field;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A class filled with debugging goodies
 * @author Barak Itkin
 */
public class Debug {

    /**
     * A global parameter indicating whether debugging info should be printed or
     * not.
     */
    public static boolean DEBUG = false;
    /**
     * A temporary directional light vector
     */
    public static Vector3 LightGlobal = Vector3.Normalize(new Vector3(1,4,5));

    /**
     * A temporary method for giving color to a surface
     */
    public static Color getFromNormal (Surface sf, Vector3 normal)
    {

        float light = Math.abs(Vector3.InnerProduct(normal, LightGlobal));
        return new Color(sf.mtl_diffuse.r * light, sf.mtl_diffuse.g * light, sf.mtl_diffuse.b * light, 1);
        //return new Color(Math.abs(normal.x), Math.abs(normal.y), Math.abs(normal.z), 1);
    }

    /**
     * Make a string representation of field values
     */
    public static String makeString(Object obj) {
        if (obj instanceof Color) {
            Color c = (Color) obj;
            return "rgba(" + c.r + "," + c.g + "," + c.b + "," + c.a + ")";
        } else if (obj instanceof Vector3) {
            Vector3 v = (Vector3) obj;
            return "vec3D(" + v.x + "," + v.y + "," + v.z + ")";
        } else {
            return (obj != null) ? obj.toString() : null;
        }

    }

    /**
     * If DEBUG is set to true, print the structure of an object
     */
    public static void print (Object obj) {
        if (!DEBUG)
            return;
        if (obj == null || obj instanceof String || obj instanceof Float || obj instanceof Integer || obj instanceof Boolean || obj instanceof Double) {
            System.err.println(obj);
        } else {
            System.err.println(obj.getClass().getName() + ":");
            for (Field field : obj.getClass().getFields()) {
                try {
                    System.err.println("  " + field.getName() + " = " + makeString(field.get(obj)));
                } catch (IllegalArgumentException ex) {
                    Logger.getLogger(Debug.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IllegalAccessException ex) {
                    Logger.getLogger(Debug.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        System.err.println();
    }
}
