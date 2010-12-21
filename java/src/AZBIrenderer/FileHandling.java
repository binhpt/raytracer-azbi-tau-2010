/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package AZBIrenderer;

import java.io.File;

/**
 *
 * @author user
 */
public class FileHandling {

    public static String path = "";

    public static void setFile(File sceneFile)
    {
        path = sceneFile.getParent() + File.separator;
    }
    public static File forPath(String p)
    {
        return new File(path + p);
    }

}
