
package AZBIrenderer;

/**
 * Some of the objects that will be created using the reflection based parsing
 * are not really new objects, but wrappers around other objects. This interface
 * is for specifying which objects are just wrappers, and it defines a way to
 * get the "Real" objects from the wrapper
 * @author Barak Itkin
 */
public interface ReflectionWrapper extends ReflectionConstructed {

    /**
     * Get the list of the actual objects that should be added to the scene
     * @return The actual objects that should be added to the scene
     */
    public Object[] getRealObjects();

}
