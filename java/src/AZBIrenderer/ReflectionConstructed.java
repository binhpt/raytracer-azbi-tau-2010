package AZBIrenderer;

/**
 * An interface that specifies which classes are allowed to be constructed by
 * reflection (by parsing a config file). This also provides a method for
 * objects to fill in the details that weren't initialized by the constructor
 * @author Barak Itkin
 */
public interface ReflectionConstructed {
    /**
     * A method that will be called by the reflection parser, once it finished
     * initializing the object by reading all the information from the config
     * file.
     * Objects can use this method to initialize the other fields that depend
     * on the ones that were parsed from the files.
     */
    public void fillMissing();
}
