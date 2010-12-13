package AZBIrenderer;

/**
 * An interface for all surfaces - they should have both a material and
 * implement basic geometric functions. However, there is a reason for this
 * separation. Some surfacess may have their own material (objects derived from
 * {@link SingleMaterialSurface}) while others may share their material with
 * other objects (objects derived from {@link Face}.
 *
 * @author Barak Itkin
 */
public interface Surface extends SurfaceMat, SurfaceGeometry {

}
