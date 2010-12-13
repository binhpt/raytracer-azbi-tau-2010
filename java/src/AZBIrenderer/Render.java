package AZBIrenderer;

import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
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

    public void render(final int screenWidth, final int screenHeight,
            final int xParts, final int yParts, final int threadCount) {
        Graphics2D g;

        ConfigParser parser = new ConfigParser(this);
        parser.Parse(config);

        render = new BufferedImage(screenWidth, screenHeight, BufferedImage.TYPE_INT_ARGB);
        g = (Graphics2D) render.getGraphics();
        g.setColor(new java.awt.Color(scene.background_col.r, scene.background_col.g, scene.background_col.b));
        g.fillRect(0, 0, screenWidth, screenHeight);
        if (scene.background_tex != null) {
            try {
                AffineTransform scaleTrans = new AffineTransform();
                BufferedImage bg_tex = ImageIO.read(new File(scene.background_tex));
                scaleTrans.scale(screenWidth / (double) bg_tex.getWidth(), screenHeight / (double) bg_tex.getHeight());
                g.drawImage(bg_tex, scaleTrans, null);
            } catch (IOException ex) {
                System.err.println("An error occured while reading the background image " + scene.background_tex);
            }
        }

        // Inverted because of the right hand coordinate system
        camera.screen_height = camera.screen_width * ((float) screenHeight / screenWidth);

        camera.bot_left = add(camera.eye, mul(camera.screen_dist, camera.direction));
        camera.bot_left = sub(camera.bot_left, mul(camera.screen_width / 2, camera.right_direction));
        camera.bot_left = sub(camera.bot_left, mul(camera.screen_height / 2, camera.up_direction));

        Thread[] threads = new Thread[threadCount];
        final List<ImagePart> parts = Collections.synchronizedList(new ArrayList<ImagePart>());
        final AtomicInteger in = new AtomicInteger(0);

        for (int i = 0; i < xParts; i++) {
            for (int j = 0; j < yParts; j++) {
                parts.add(new ImagePart((i * screenWidth) / xParts, (j * screenHeight) / yParts,
                        ((i + 1) * screenWidth) / xParts - 1, ((j + 1) * screenHeight) / yParts - 1));
            }
        }

        for (int i = 0; i < threads.length; i++) {
            threads[i] = new Thread(new Runnable() {

                public void run() {
                    int part;
                    while ((part = in.getAndIncrement()) < xParts * yParts) {
                        ImagePart im = parts.get(part);
                        int sampleCount = scene.super_samp_width, samplesPerPixel = sampleCount * sampleCount;
                        float sampleDistX = 1.0f / (sampleCount + 1) / screenWidth;
                        float sampleDistY = 1.0f / (sampleCount + 1) / screenHeight;
                        Ray r;

                        int color;

                        for (int i = im.yStart; i <= im.yEnd; i++) {
                            for (int j = im.xStart; j <= im.xEnd; j++) {
                                Color[][] samples = new Color[sampleCount][sampleCount];

                                float y = ((float) i) / screenHeight;
                                float x = ((float) j) / screenWidth;

                                for (int a = 0; a < sampleCount; a++) {
                                    for (int b = 0; b < sampleCount; b++) {
                                        //r = camera.CreateRay(y + (a+(float)Math.random())*sampleDistY, x + (b+(float)Math.random())*sampleDistX);
                                        r = camera.CreateRay(y + (a + (float)Math.random()) * sampleDistY, x + (b + (float)Math.random()) * sampleDistX);
                                        //r = camera.CreateRay(y, x);
                                        samples[a][b] = ShootRay(r);
                                    }
                                }

                                float red = 0, green = 0, blue = 0;
                                int hits = 0;

                                for (Color[] pixelRow : samples) {
                                    for (Color pixel : pixelRow) {
                                        if (pixel != null)
                                        {
                                            red += pixel.r;
                                            green += pixel.g;
                                            blue += pixel.b;
                                            hits++;
                                        }
                                    }
                                }

                                if (hits != 0) {
                                    color = new Color(red / samplesPerPixel, green / samplesPerPixel, blue / samplesPerPixel, hits / (float)samplesPerPixel).getRGB();
                                    im.setRGB(j - im.xStart, i - im.yStart, color);
                                }
                            }
                        }
                    }

                }
            });
            threads[i].start();
        }

        for (int i = 0; i < threads.length; i++) {
            try {
                threads[i].join();
            } catch (InterruptedException ex) {
                System.err.print("A serious synchronization error occured...");
                System.err.print("Please try the single threaded execution.");
            }
        }

        BufferedImage accumulate = new BufferedImage(screenWidth, screenHeight, BufferedImage.TYPE_INT_ARGB);

        for (ImagePart imagePart : parts) {
            for (int i = 0; i < imagePart.height; i++) {
                for (int j = 0; j < imagePart.width; j++) {
                    accumulate.setRGB(j + imagePart.xStart, i + imagePart.yStart, imagePart.im[i][j]);
                }
            }
        }

        g.drawImage(accumulate, 0, 0, null);
    }

    public static boolean shootAtSurfaces(Iterable<? extends Surface> surfaces, Ray r,
            IntersectionData closestIntersect) {
        boolean intersect = false;
        IntersectionData temp = new IntersectionData();

        closestIntersect.T = Float.MAX_VALUE;

        for (Surface surf : surfaces) //if there is a collision, and its T is smaller, this is the new closest collision
        {
            if (surf.Intersection(r, temp) && temp.T < closestIntersect.T) {
                closestIntersect.copyFrom(temp);
                intersect = true;
            }
        }
        return intersect;
    }


    /*
     * similar to shootAtSurfaces, but more efficient because it does less
     */
    public static boolean ShootLightAtSurfaces(List<? extends Surface> surfaces, Ray r, float maxT) {
        IntersectionData temp = new IntersectionData();
        for (Surface surf : surfaces) {
            if (surf.Intersection(r, temp) && temp.T < maxT && temp.T > 0.001f)
            {
                return false;
            }
        }
        return true;
    }

    /* Returns null in case of no intersection */
    public Color ShootRay(Ray r) {
        IntersectionData intersect = new IntersectionData();

        IntersectionData lightIntersection = new IntersectionData();
        lightIntersection.T = Float.MAX_VALUE;
        Ray lightray = new Ray();

        Color color = new Color(0, 0, 0, 1);
        Color tc, mtlDiffuse, mtlSpecular;

        double diffuse, specular1, specular2;
        Vector3 H;
        float dist;

        if (shootAtSurfaces(surfaces, r, intersect)) {

            /*
             * rough draft still, uses only LightDirected.
             */
            for (Light light : lights) {
                //OPTIMIZE
                dist = light.GetRay(intersect.point, lightray);
                //Vector3 temp = new Vector3(-intersect.normal.x, -intersect.normal.y, -intersect.normal.z);
                //if (Vector3.InnerProduct(lightray.direction, intersect.normal) < 0)
//                    continue;
                //lightray.origin = add(lightray.origin, mul(Float.MIN_VALUE, lightray.direction));
                //float.maxvalue is only for LightDirected, change later
                if (ShootLightAtSurfaces(surfaces, lightray, dist)) //if (lightIntersection.point.equals(closestIntersect.point))
                {
                    //just for testing, need to OPTIMIZE.. alot
                    tc = light.EffectFromLight(intersect.point); //I(L) in the presentation

                    //diffuse component: K(d) * NL * I(L)
                    lightray.direction = Normalize(lightray.direction);
                    //lightray.direction = Normalize(new Vector3(-lightray.direction.x, -lightray.direction.y, -lightray.direction.z));
                    //ambient = InnerProduct(intersect.normal, lightray.direction);
                    //if (ambient < 0) ambient = 0;
                    intersect.normal = Normalize(intersect.normal);
                    diffuse = InnerProduct(intersect.normal, lightray.direction); //N*L
                    if (diffuse < 0) diffuse = 0;

                    //specular component: K(s) * (VR)^n *I(L)
                    H = Normalize(sub(r.direction, lightray.direction));
                    specular1 = InnerProduct(H, intersect.normal);
                    //if (specular1 < 0) specular1 = 0;
                    specular2 = Math.pow(specular1, intersect.surface.getMtl_shininess());
                    //specular2 = 0;

                    mtlDiffuse = intersect.surface.getMtl_diffuse();
                    mtlSpecular = intersect.surface.getMtl_specular();
                    color.r += tc.r * (mtlDiffuse.r * diffuse + specular2 * mtlSpecular.r);
                    color.g += tc.g * (mtlDiffuse.g * diffuse + specular2 * mtlSpecular.g);
                    color.b += tc.b * (mtlDiffuse.b * diffuse + specular2 * mtlSpecular.b);

                }
            }

            color.r += this.scene.ambient_light.r * intersect.surface.mtl_ambient.r;
            color.g += this.scene.ambient_light.g * intersect.surface.mtl_ambient.g;
            color.b += this.scene.ambient_light.b * intersect.surface.mtl_ambient.b;

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
