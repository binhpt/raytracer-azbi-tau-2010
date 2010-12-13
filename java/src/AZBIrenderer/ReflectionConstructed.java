package AZBIrenderer;

/**
 * An interface that specifies which classes are allowed to be constructed by
 * reflection (by parsing a config file). This also provides a method for
 * objects to fill in the details that weren't initialized by the process of
 * parsing or by the empty constructor.
 *
 * Objects implementing this interface, must be carefully named and their fields
 * must also be carefully named! The names of the fields to initialize and the
 * names of the object to create are taken directly from the config file! The
 * name conversion is pretty straight forward:
 * <table>
 * <tr><td></td><th>Config name</th><th>Java Name</th></tr>
 * <tr><th>Object types</th><td>my-foo-object</td><td>MyFooObject</td>
 * <tr><th>Field names</th><td>his-bar-field</td><td>his_bar_field</td>
 * </table>
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
