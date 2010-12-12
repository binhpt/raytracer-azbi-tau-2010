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
 * A parser for the scene config files
 * @author Adam Zeira & Barak Itkin
 */
public class ConfigParser {

    public Render render;
    protected HashMap<String, String> props;
    protected String className;

    /**
     * Create a parser for a config file, for a specified Render object
     * @param render The Render object to parse into
     */
    public ConfigParser(Render render) {
        this.render = render;
        this.props = new HashMap<String, String>(15);
        this.className = "";
    }

    /**
     * Called when a line declaring an object property was found
     * @param key The property name
     * @param val The property value
     */
    protected void pushProp(String key, String val) {
        if (className.isEmpty()) {
            System.err.println("Can't define properties without an object...");
            System.err.println(key + " = " + val);
        } else {
            this.props.put(key, val);
        }
    }

    /**
     * Called when a line declaring a new object was found
     * @param objType The type of the object
     */
    protected void pushObject(String objType) {
        /* Ignore empty definitions or the empty before the first object */
        Object[] single = new Object[1];
        Object[] children;

        if (!this.className.isEmpty() && !this.props.isEmpty()) {
            Object obj = ReflectionParse(this.className, props);
            
            if (obj instanceof ReflectionWrapper)
            {
                children = ((ReflectionWrapper)obj).getRealObjects();
            }
            else
            {
                single[0] = obj;
                children = single;
            }

            for (Object sceneObj : children) {
                if (sceneObj instanceof Light) {
                    this.render.lights.add((Light) sceneObj);
                } else if (sceneObj instanceof Surface) {
                    this.render.surfaces.add((Surface) sceneObj);
                } else if (sceneObj instanceof Camera) {
                    this.render.camera = (Camera) sceneObj;
                } else if (sceneObj instanceof Scene) {
                    this.render.scene = (Scene) sceneObj;
                }
            }
            props.clear();
        }
        this.className = objType;
    }

    /**
     * Parse a config file and add it's definitions to the Render object
     * @param config A string containing the actual content of the config file
     */
    public void Parse(String config) {
        String line;
        int splitLocation;

        BufferedReader iss = new BufferedReader(new StringReader(config));
        try {
            while (iss.ready() && (line = iss.readLine()) != null) {
                if (!(line = line.trim()).isEmpty()) {
                    if (line.startsWith("#"))
                        continue;
                    else if (line.endsWith(":")) {
                        pushObject(line.substring(0, line.length()-1));
                    } else if ((splitLocation = line.indexOf("=")) != -1) {
                        pushProp(line.substring(0, splitLocation), line.substring(splitLocation+1));
                    }
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(ConfigParser.class.getName()).log(Level.SEVERE, null, ex);
        }
        /* Add the last object! */
        pushObject("");
    }

    /**
     * Parse a color property
     * @param line A string containing the color property
     * @return The matching Color
     */
    public static Color GetColorParam(String line) {
        Color c = new Color();

        String[] components = line.trim().replaceAll("\\s+", " ").split(" ");
        c.r = Float.parseFloat(components[0]);
        c.g = Float.parseFloat(components[1]);
        c.b = Float.parseFloat(components[2]);
        c.a = 1;

        return c;
    }

    /**
     * Parse a vector property, and return a normalized vector!
     * "parameters that are of type "vector" must be normalized to be of length
     *  1 by your code"
     * @param line A string containing the vector property
     * @return The matching normalized Vector3
     */
    public static Vector3 GetVectorParam(String line) {
        Vector3 v = new Vector3();

        String[] components = line.trim().replaceAll("\\s+", " ").split(" ");
        v.x = Float.parseFloat(components[0]);
        v.y = Float.parseFloat(components[1]);
        v.z = Float.parseFloat(components[2]);
        
        return Vector3.Normalize(v);
    }

    /**
     * Parse a point property
     * @param line A string containing the point property
     * @return The matching Point3
     */
    public static Point3 GetPointParam(String line) {
        Point3 v = new Point3();

        String[] components = line.trim().replaceAll("\\s+", " ").split(" ");
        v.x = Float.parseFloat(components[0]);
        v.y = Float.parseFloat(components[1]);
        v.z = Float.parseFloat(components[2]);

        return v;
    }

    /**
     * Parse a shader property.
     * @param line A string containing the shader name
     * @return The matching shader
     */
    public Mesh.Shader GetShaderParam(String line) {
        line = line.trim().replaceAll("\\s+", " ");
        if (line.equals("flat"))
            return Mesh.Shader.FLAT;
        else if(line.equals("phong"))
            return Mesh.Shader.PHONG;
        else
            throw new IllegalArgumentException("Bad shader type! \"" + line + "\"");
    }

    /**
     * Parse an integer property.
     * Provided for completness only.
     * @param line A string containing the integer property
     * @return The matching int
     */
    public int GetIntParam(String line) {
        return Integer.parseInt(line);
    }

    /**
     * Parse a float property.
     * Provided for completness only.
     * @param line A string containing the float property
     * @return The matching float
     */
    public float GetFloatParam(String line) {
        return Float.parseFloat(line);
    }

    /**
     * Parse a boolean property (provided as '0' or '1')
     * @param line A string containing the boolean property
     * @return The matching boolean
     */
    public boolean GetBooleanParam(String line) {
        return Integer.parseInt(line) == 1;
    }

    /**
     * Given an object name and attribute from the config file, create an
     * instance of it (as the name indicates, it's based on reflection for
     * doing this generically instead of writing specific code for each object)
     * @param objType The name of the object (from the config file)
     * @param props The properties of the object (from the config file)
     * @return An object representing the given data
     */
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
                } else if (f.getType() == Mesh.Shader.class) {
                    f.set(obj, GetShaderParam(props.get(key)));
                } else if (f.getType() == String.class) {
                    f.set(obj, props.get(key).trim());
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
