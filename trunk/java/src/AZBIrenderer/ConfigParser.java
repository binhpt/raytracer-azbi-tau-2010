/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package AZBIrenderer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author user
 */
public class ConfigParser {

    public Render render;
    protected HashMap<String, String> props;
    protected String className;

    public ConfigParser(Render render) {
        this.render = render;
        this.props = new HashMap<String, String>(15);
        this.className = "";
    }

    public void pushProp(String key, String val) {
        if (className.isEmpty()) {
            System.err.println("Can't define properties without an object...");
            System.err.println(key + " = " + val);
        } else {
            this.props.put(key, val);
        }
    }

    public void pushObject(String className) {
        /* Ignore empty definitions or the empty before the first object */
        if (!this.className.isEmpty() && !this.props.isEmpty()) {
            Object obj = ReflectionParse(this.className, props);
            if (obj instanceof Light) {
                this.render.lights.add((Light) obj);
            } else if (obj instanceof Surface) {
                this.render.surfaces.add((Surface) obj);
            } else if (obj instanceof Camera) {
                this.render.camera = (Camera) obj;
            } else if (obj instanceof Scene) {
                this.render.scene = (Scene) obj;
            }
            props.clear();
        }
        this.className = className;
    }

    public void Parse(String config) {
        String line;
        int splitLocation;

        BufferedReader iss = new BufferedReader(new StringReader(config));
        try {
            while (iss.ready() && (line = iss.readLine()) != null) {
                if (!line.isEmpty()) {
                    if (line.endsWith(":")) {
                        pushObject(line.substring(0, line.length()-1));
                    } else if ((splitLocation = line.indexOf("=")) != -1) {
                        pushProp(line.substring(0, splitLocation), line.substring(splitLocation+1));
                    }
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(ConfigParser.class.getName()).log(Level.SEVERE, null, ex);
        }
        pushObject("");
    }

    public static Color GetColorParam(String line) {
        Color c = new Color();

        String[] components = line.trim().replaceAll("\\s+", " ").split(" ");
        c.r = Float.parseFloat(components[0]);
        c.g = Float.parseFloat(components[1]);
        c.b = Float.parseFloat(components[2]);
        c.a = 1;

        return c;
    }

    /* "parameters that are of type "vector" must be normalized to be of length
     *  1 by your code".
     * OK, makes sense - this means we won't need to normalize vectors later!
     */
    public static Vector3 GetVectorParam(String line) {
        Vector3 v = new Vector3();

        String[] components = line.trim().replaceAll("\\s+", " ").split(" ");
        v.x = Float.parseFloat(components[0]);
        v.y = Float.parseFloat(components[1]);
        v.z = Float.parseFloat(components[2]);
        
        return Vector3.Normalize(v);
    }

    /* Unlike the vector parameter, this shouldn't be normalized!!
     * And I figured the mistake of auto normalizing points (which were
     * represented as vectors, only after a lot of debugging :P)
     */
    public static Point3 GetPointParam(String line) {
        Point3 v = new Point3();

        String[] components = line.trim().replaceAll("\\s+", " ").split(" ");
        v.x = Float.parseFloat(components[0]);
        v.y = Float.parseFloat(components[1]);
        v.z = Float.parseFloat(components[2]);

        return v;
    }

    public int GetIntParam(String line) {
        return Integer.parseInt(line);
    }

    public float GetFloatParam(String line) {
        return Float.parseFloat(line);
    }

    /* Booleans are specified as 0 or 1 */
    public boolean GetBooleanParam(String line) {
        return Integer.parseInt(line) == 1;
    }

    public Object ReflectionParse(String objType, Map<String, String> props) {
        Object obj = null;
        Class c = null;
        Field f = null;
        Class fType = null;
        String fieldName = null;

        String className = "";
        String temp = null;
        Class p;

        boolean safe = false;

        /* Generate the class name from the config file name:
         * Capitalize the begining of each word, and remove the '-' signs
         */
        for (String word : objType.split("\\-")) {
            temp = word.toLowerCase();
            className += Character.toUpperCase(temp.charAt(0)) + temp.substring(1);
        }

        try {
            c = Class.forName(ConfigParser.class.getPackage().getName() + "." + className);
            
            /* Now, to prevent someone to abuse this interface, for safety
             * reasons we defined the ReflectionConstructed interface that in
             * addition to it's functionallity, will allow us to prevent someone
             * from writing malicous config files that will create various system
             * objects
             */
            p = c;
            while (!safe && p != null)
            {
                for (Class inter : p.getInterfaces()) {
                    if (safe = (inter == ReflectionConstructed.class))
                        break;
                }
                p = p.getSuperclass();
            }
            if (!safe)
                throw new ClassNotFoundException();

            obj = c.newInstance();

            /*
            System.err.println("   Fields of " + className);
            for (Field field : c.getFields()) {
                System.err.println("   " + field.getName());
            }
             */
            for (String key : props.keySet()) {
                fieldName = key.trim().replace('-', '_');
                f = c.getField(fieldName);
                if (f.getType() == int.class) {
                    f.setInt(obj, GetIntParam(props.get(key)));
                } else if (f.getType() == float.class) {
                    f.setFloat(obj, GetFloatParam(props.get(key)));
                } else if (f.getType() == boolean.class) {
                    f.setBoolean(obj, GetBooleanParam(props.get(key)));
                } else if (f.getType() == Point3.class) {
                    f.set(obj, GetPointParam(props.get(key)));
                } else if (f.getType() == Vector3.class) {
                    f.set(obj, GetVectorParam(props.get(key)));
                } else if (f.getType() == Color.class) {
                    f.set(obj, GetColorParam(props.get(key)));
                }
            }

            ((ReflectionConstructed)obj).fillMissing();

        } catch (NoSuchFieldException ex) {
            System.err.println("Invalid property " + fieldName + " of " + objType);
        } catch (ClassNotFoundException ex) {
            System.err.println("No such object type " + objType);
        } catch (Exception ex) {
            Logger.getLogger(ConfigParser.class.getName()).log(Level.SEVERE, null, ex);
        }
        Debug.print(obj);
        return obj;

    }
}
