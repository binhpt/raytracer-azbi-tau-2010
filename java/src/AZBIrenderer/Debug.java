/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package AZBIrenderer;

import java.lang.reflect.Field;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author user
 */
public class Debug {

    public static boolean DEBUG = false;
    
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
