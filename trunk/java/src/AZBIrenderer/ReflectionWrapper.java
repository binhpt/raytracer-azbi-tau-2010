
package AZBIrenderer;

/**
 * An interface to get the actual data from objects which are just wrappers
 * for the reflection-based parsing
 * @author Barak Itkin
 */
public interface ReflectionWrapper extends ReflectionConstructed {

    public Object[] getRealObjects();

}
