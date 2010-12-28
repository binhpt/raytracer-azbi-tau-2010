package AZBIrenderer;

import java.io.File;

/**
 * Setting the path, and getting the path
 * @author Barak Itkin & Adam Zeira
 */
public class FileHandling {

    public static String path = "";

    /**
     * Determine the scene file to be referenced
     * @param sceneFile the scene file
     */
    public static void setFile(File sceneFile)
    {
        path = sceneFile.getParent() + File.separator;
    }

    /**
     * Get a file in the location relative to the scene file
     * @param p the relative path
     * @return the matching file
     */
    public static File forPath(String p)
    {
        System.out.println(path+p);
        return new File(path + p);
    }

}
