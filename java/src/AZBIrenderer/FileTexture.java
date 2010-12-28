package AZBIrenderer;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Annotate BufferedImage objects which represent images that should be loaded
 * from a file
 * @author Barak Itkin
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface FileTexture {

}
