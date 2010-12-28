package AZBIrenderer;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import static AZBIrenderer.Vector3.*;

/**
 * This class has all the data needed to produce a render from a config file.
 * The actual rendering happens here!
 * @author Barak Itkin & Adam Zeira
 */
public class Render {

    //minimum distance that must be passed to accept an intersection with an object (to avoid self-collision of some objects)
    public static final double min_intersect_T = 0.00001;
    /**
     * A class for representing a part of an image that should be rendered
     */
    public static class ImagePart {

        int xStart, yStart, xEnd, yEnd;
        int width, height;
        int[][] im;

        public ImagePart(int xStart, int yStart, int xEnd, int yEnd) {
            this.xStart = xStart;
            this.yStart = yStart;
            this.xEnd = xEnd;
            this.yEnd = yEnd;
            this.width = xEnd - xStart + 1;
            this.height = yEnd - yStart + 1;
            this.im = new int[height][width];
        }

        public void setRGB (int x, int y, int rgb)
        {
            im[y][x]= rgb;
        }
    }

    /**
     * A list of all the geometric surfaces which should be rendered
     */
    public List<Surface> surfaces;
    public OctalTree octree;
    /**
     * A list of all the lights
     */
    public List<Light> lights;
    /**
     * The scene's camera
     */
    public Camera camera;
    /**
     * An object contatining various scene settings
     */
    public Scene scene;
    /**
     * A string containing the config file (it's content, not it's path) with
     * the scene definitions
     */
    public String config;

    /**
     * The result of the actual render
     */
    protected BufferedImage render;

    public Render(String config) {
        this.surfaces = new ArrayList<Surface>();
        this.lights = new ArrayList<Light>();
        this.config = config;
    }

    /**
     * The main process for the rendering, The result will be saved inside
     * the render field of this class.
     * @param resultWidth The width of the result image
     * @param resultHeight The height of the result image
     * @param xParts The amount of parts along the X-axis that the work should be split to
     * @param yParts The amount of parts along the Y-axis that the work should be split to
     * @param threadCount The amount of threads to use
     */
    public void render(final int resultWidth, final int resultHeight,
            final int xParts, final int yParts, final int threadCount) {

        /* Create a parser and parse this scene */
        ConfigParser parser = new ConfigParser(this);
        parser.Parse(config);

        if (scene.use_acceleration && surfaces.size() > 64)
        {
            System.out.println("Building Octree");
            int depth = (int) (Math.log(surfaces.size()) / Math.log(64));
            octree = new OctalTree(surfaces, depth);
        }

        /* Create the result image */
        render = new BufferedImage(resultWidth, resultHeight, BufferedImage.TYPE_INT_ARGB);
        /* Fill it with a background color or a specified background image */
        Graphics2D g = (Graphics2D) render.getGraphics();
        g.setColor(new java.awt.Color(scene.background_col.r, scene.background_col.g, scene.background_col.b));
        g.fillRect(0, 0, resultWidth, resultHeight);
        if (scene.background_tex != null) {
            AffineTransform scaleTrans = new AffineTransform();
            scaleTrans.scale(resultWidth / (double) scene.background_tex.getWidth(), resultHeight / (double) scene.background_tex.getHeight());
            g.drawImage(scene.background_tex, scaleTrans, null);
        }

        /* Initialize some fields in the camera. Note that some values seem
         * to be switched/inverted - this is because of the right hand
         * coordinate system. See the Camera class for more details
         */
        camera.screen_height = camera.screen_width * ((double) resultHeight / resultWidth);

        camera.bot_left = add(camera.eye, mul(camera.screen_dist, camera.direction));
        camera.bot_left = sub(camera.bot_left, mul(camera.screen_width / 2, camera.right_direction));
        camera.bot_left = sub(camera.bot_left, mul(camera.screen_height / 2, camera.up_direction));

        /* Initialize some fields for synchronizing between the threads */
        Thread[] threads = new Thread[threadCount];
        final AtomicInteger partsDone = new AtomicInteger(0);
        final List<ImagePart> parts = Collections.synchronizedList(new ArrayList<ImagePart>());

        /* Split the work on the entire image to smaller parts */
        for (int i = 0; i < xParts; i++) {
            for (int j = 0; j < yParts; j++) {
                parts.add(new ImagePart((i * resultWidth) / xParts, (j * resultHeight) / yParts,
                        ((i + 1) * resultWidth) / xParts - 1, ((j + 1) * resultHeight) / yParts - 1));
            }
        }

        System.out.println("Start Rendering");
        System.out.println(this.surfaces.size() + " faces");
        System.out.println(this.lights.size() + " lights");
        /* Create the rendering threads */
        for (int i = 0; i < threads.length; i++) {
            threads[i] = new Thread(new Runnable() {

                /* The actual rendering by each thread is done here */
                public void run() {
                    Ray r;
                    int color;

                    /* The number of the part we should work on */
                    int part;

                    /* Antialiasing parameters */
                    int sampleCount = scene.super_samp_width, samplesPerPixel = sampleCount * sampleCount;
                    /* The distance between samples, in image relative (0-1)
                     * coordinates
                     */
                    double sampleDistX = 1.0 / (sampleCount + 1) / resultWidth;
                    double sampleDistY = 1.0 / (sampleCount + 1) / resultHeight;

                    /* Check that there are still some unrendered parts */
                    while ((part = partsDone.getAndIncrement()) < xParts * yParts) {
                        /* Get the part that we should work on */
                        ImagePart im = parts.get(part);

                        /* For each pixel */
                        for (int i = im.yStart; i <= im.yEnd; i++) {
                            for (int j = im.xStart; j <= im.xEnd; j++) {

                                /* The default color for this pixel */
                                color = Color.TRANSPARENT.getRGB();

                                /* Do we preform supersampling? */
                                if (sampleCount != 1) {
                                    int hits = 0;
                                    /* Create an array of samples */
                                    Color[][] samples = new Color[sampleCount][sampleCount];

                                    /* Find the location of the pixel */
                                    double y = ((double) i) / resultHeight;
                                    double x = ((double) j) / resultWidth;

                                    for (int a = 0; a < sampleCount; a++) {
                                        for (int b = 0; b < sampleCount; b++) {
                                            /* Create the ray to shoot */
                                            r = camera.CreateRay (y + (a + Math.random()) * sampleDistY,
                                                    x + (b + Math.random()) * sampleDistX);
                                            /* Save the color resulted from shooting it */
                                            if (j==195 && i==195)
                                                System.err.println("here");
                                            samples[a][b] = ShootRay(r, scene.max_ray_bounce);
                                        }
                                    }

                                    float red = 0, green = 0, blue = 0;

                                    /* Compute the averege color from all the samples*/
                                    for (Color[] pixelRow : samples) {
                                        for (Color pixel : pixelRow) {
                                            if (pixel != null) {
                                                red += pixel.r;
                                                green += pixel.g;
                                                blue += pixel.b;
                                                hits++;
                                            }
                                        }
                                    }

                                    /* Store the result in the variable representing
                                     * the color of this pixel
                                     */
                                    color = new Color(red / hits,
                                            green / hits,
                                            blue / hits,
                                            hits / (float) samplesPerPixel).getRGB();

                                } else { /* We don't do super sampling */
                                    /* Find the location of the pixel */
                                    double y = ((double) i) / resultHeight;
                                    double x = ((double) j) / resultWidth;
                                    r = camera.CreateRay(y, x);
                                    Color pixel = ShootRay(r, scene.max_ray_bounce);
                                    if (pixel != null) {
                                        color = pixel.getRGB();
                                    }
                                }

                                /* Put the color inside the image buffer */
                                im.setRGB(j - im.xStart, i - im.yStart, color);
                            }
                        }
                    System.out.println("Finished part " + (part+1) + "/" + (xParts * yParts));
                    }
                    System.out.println("Thread finished");
                }
            });

            /* Start the thread */
            threads[i].start();
        }

        /* Wait for all threads to finish */
        for (int i = 0; i < threads.length; i++) {
            try {
                threads[i].join();
            } catch (InterruptedException ex) {
                System.err.print("A serious synchronization error occured...");
                System.err.print("Please try the single threaded execution.");
                return;
            }
        }

        System.out.println("Composing the final image");
        /* Create an image into which we accumulate the different parts of the
         * rendered image
         */
        BufferedImage accumulate = new BufferedImage(resultWidth, resultHeight, BufferedImage.TYPE_INT_ARGB);

        for (ImagePart imagePart : parts) {
            for (int i = 0; i < imagePart.height; i++) {
                for (int j = 0; j < imagePart.width; j++) {
                    accumulate.setRGB(j + imagePart.xStart, i + imagePart.yStart, imagePart.im[i][j]);
                }
            }
        }

        /* Draw the result above the background computed earlier */
        g.drawImage(accumulate, 0, 0, null);
    }

    /**
     * Shoot a ray at a list of surfaces, and return the closest intersect.
     * THE DIRECTION (SIGN) OF THE RAY MATTERS! ONLY TAKE INTERSECTIONS WHICH
     * ARE AT A POSITIVE T FROM THE ORIGIN OF THE RAY!
     */
    public boolean shootAtSurfaces(Ray r, IntersectionData closestIntersect) {
        boolean intersect = false;
        IntersectionData temp = new IntersectionData();
        List<Surface> Rsurfaces = octree == null ? this.surfaces : octree.getObjects(r);

        closestIntersect.T = Double.MAX_VALUE;

        r.origin = add(r.origin, mul(Math3D.GEOM_EPSILON, r.direction));

        for (Surface surf : Rsurfaces) //if there is a collision, and its T is smaller, this is the new closest collision
        {
            // If the intersection is when T is negative, then we doon't want it!
            if (surf.Intersection(r, temp, !surf.getMtl_type().equals("flat")) && temp.T > min_intersect_T && temp.T < closestIntersect.T) {
                closestIntersect.copyFrom(temp);
                intersect = true;
            }
        }
        return intersect;
    }


    /**
     * similar to shootAtSurfaces, but more efficient because it does less,
     * it just checks if there is an intersection or not
     */
    public boolean ShootLightAtSurfaces(Ray r, double maxT) {
        IntersectionData temp = new IntersectionData();
        List<Surface> Rsurfaces = octree == null ? this.surfaces : octree.getObjects(r);
        for (Surface surf : Rsurfaces) {
            if (surf.Intersection(r, temp, false) && temp.T > min_intersect_T && temp.T < maxT)
            {
                return false;
            }
        }
        return true;
    }

    /**
     * Shoot a ray and compute the color it produces
     */
    public Color ShootRay(Ray r, int raybounces) {
        IntersectionData intersect = new IntersectionData();
        LinkedList<Ray> lightrays = new LinkedList<Ray>();

        Color color = new Color(0, 0, 0, 1);
        Color tc, mtlDiffuse, mtlSpecular;

        double diffuse, specular1, specular2, dist;

        //if the ray (from the camera or a reflection) intersects with something
        if (shootAtSurfaces(r, intersect)) {

            mtlDiffuse = intersect.surface.GetDiffuse2(intersect.u, intersect.v);
            mtlSpecular = intersect.surface.getMtl_specular();
            
            intersect.normal = Normalize(intersect.normal);

            //now that we have a point of intersection, see what lights in the scene effect it
            for (Light light : lights) {

                //hemisphere, a special case, no need to check collision etc
                if (light instanceof LightHemisphere)
                {
                    tc = light.EffectFromLight(intersect.normal);
                    color.r += tc.r * mtlDiffuse.r;
                    color.g += tc.g * mtlDiffuse.g;
                    color.b += tc.b * mtlDiffuse.b;
                }
                
                //all other ights
                else
                {
                    ////////////////////////////////////////////////////////////
                    /// Lighting - see the documentation file for a detailed ///
                    /// explanation                                          ///
                    ////////////////////////////////////////////////////////////
                    dist = light.GetRay(intersect.point, lightrays);
                    //for lights with many rays. iterate over each one
                    for (Ray lightray : lightrays)
                    {
                        //check if the light ray reaches our point. add the lights specular and diffuse
                        if (ShootLightAtSurfaces(lightray, dist))
                        {
                            tc = light.EffectFromLight(intersect.point); //I(L) in the presentation

                            // diffuse component: K(d) * NL * I(L)
                            // Note that lightray.direction is the direction from
                            // from the point to the light source!
                            diffuse = InnerProduct(intersect.normal, lightray.direction); //N*L

                            //////////////////////////////////////////////////
                            /// PRETEST - start                            ///
                            //////////////////////////////////////////////////

                            // sign(-diffuse) = whether the light to the point (from the source) is
                            //                  the same direction as the normal
                            // sign(<N,R>) = whether the ray from the camera is the same direction
                            //               as the normal
                            // We want to check that both the light and viewer are on the front side
                            // of the surface
                            if (- diffuse > 0 || InnerProduct(intersect.normal, r.direction) > 0) continue;

                            //////////////////////////////////////////////////
                            /// PRETEST - end                              ///
                            //////////////////////////////////////////////////

    //                        // cheap specular component: K(s) * (HN)^n *I(L)
    //                        H = Normalize(sub(r.direction, lightray.direction));
    //                        specular1 = InnerProduct(H, intersect.normal);

                            // expensive specular component: K(s) * (VR)^n *I(L)
                            Vector3 ref = Vector3.sub(Vector3.mul(InnerProduct(lightray.direction, intersect.normal) * 2, intersect.normal), lightray.direction);
                            specular1 = Vector3.InnerProduct(r.direction, ref);
                            specular2 = Math.pow(Math.abs(specular1), intersect.surface.getMtl_shininess());

                            //////////////////////////////////////////////////
                            /// POSTTEST - put here                        ///
                            //////////////////////////////////////////////////

                            color.r += tc.r * (mtlDiffuse.r * diffuse + specular2 * mtlSpecular.r);
                            color.g += tc.g * (mtlDiffuse.g * diffuse + specular2 * mtlSpecular.g);
                            color.b += tc.b * (mtlDiffuse.b * diffuse + specular2 * mtlSpecular.b);
                        }
                    }
                    lightrays.clear();
                }
            }

            //handle reflection. self explanatory, as done in class
            if (raybounces > 0 && intersect.surface.reflectance > 0)
            {
                double length = InnerProduct(intersect.normal, r.direction) * -2;
                Vector3 bounce = Vector3.add(Vector3.mul(length, intersect.normal), r.direction);
                Ray reflectray = new Ray(intersect.point, bounce);
                Color reflectColor = ShootRay(reflectray, --raybounces);
                if (reflectColor == null)
                    reflectColor = this.scene.background_col;
                color.r += reflectColor.r * intersect.surface.reflectance;
                color.g += reflectColor.g * intersect.surface.reflectance;
                color.b += reflectColor.b * intersect.surface.reflectance;
            }

            //ambient light
            color.r += this.scene.ambient_light.r * intersect.surface.mtl_ambient.r;
            color.g += this.scene.ambient_light.g * intersect.surface.mtl_ambient.g;
            color.b += this.scene.ambient_light.b * intersect.surface.mtl_ambient.b;

            //clamp the color
            if (color.r > 1f) color.r = 1;
            if (color.g > 1f) color.g = 1;
            if (color.b > 1f) color.b = 1;

            if (color.r < 0f) color.r = 0;
            if (color.g < 0f) color.g = 0;
            if (color.b < 0f) color.b = 0;

            color.a = 1;

            return color;
        } else {
            return null;
        }
    }
}
