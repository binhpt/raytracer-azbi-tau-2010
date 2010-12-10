/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package AZBIrenderer;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import static AZBIrenderer.Vector3.*;

/**
 *
 * @author user
 */
public class Render {

    public List<Surface> surfaces;
    public List<Light> lights;
    public Camera camera;
    public Scene scene;
    public String config;
    protected boolean init;
    protected BufferedImage render;
    protected int hitCount;

    public Render(String config) {
        this.surfaces = new ArrayList<Surface>();
        this.lights = new ArrayList<Light>();
        this.init = false;
        this.config = config;
    }

    private void init() {
    }

    public void render(int screenWidth, int screenHeight) {
        hitCount = 0;
        Color pixel;
        Ray r;

        ConfigParser parser = new ConfigParser(this);
        parser.Parse(config);

        render = new BufferedImage(screenWidth, screenHeight, BufferedImage.TYPE_INT_ARGB);
        render.getGraphics().setColor(new java.awt.Color(scene.background_col.getRGB()));
        render.getGraphics().fillRect(0, 0, screenWidth, screenHeight);

	/****ROUGH DRAFT of how it should go:***/
	//additional camera initializations

	camera.screen_height = camera.screen_width * (((float)screenWidth) / screenHeight);

	//iterate every pixel of the display screen
	//multithreading should go here
        int color;
	for (int i = 0; i < screenHeight; i++)
		for (int j = 0; j < screenWidth; j++)
		{
			r = CreateRay(((float)i) / screenHeight, ((float)j) / screenWidth);
//                        Debug.print("\n\n\nShooting a ray!");
//                        Debug.print(r);
			//UI related- image[i, j] = ShootRay(ray);
			pixel = ShootRay(r);
                        if (pixel != null)
                        {
                            hitCount++;
                            color = pixel.getRGB();
                            render.setRGB(j, i, color);
                        }
			//1 ray for now, anti aliasing later
		}

        System.out.println("Total hits: " + hitCount);

    }

    public Color ShootRay(Ray r) {
        Color c;
        IntersectionData closestIntersect = new IntersectionData();
        IntersectionData temp = new IntersectionData();

        closestIntersect.T = Float.MAX_VALUE;

        for (Surface surf : surfaces) //if there is a collision, and its T is smaller, this is the new closest collision
        {
            if (surf.Intersection(r, temp) && temp.T < closestIntersect.T) {
                closestIntersect = temp;
            }
        }

        //we now have the closest surface and point on the surface
        //obviously enough to get the color
        //surface and point are saved for later implementations of bouncing
        //we'll also need the normal later on, either on collision or we'll get it on its own function using the intersection vector
        //for this excersize they want plain color, angles don't matter
        c = closestIntersect.col;
        return c;
    }

    Ray CreateRay(float xratio, float yratio) {
        Ray r = new Ray();
        r.origin = camera.eye;

        Vector3 dest = new Vector3(camera.eye);
        dest = add(dest, mul(camera.screen_dist, camera.direction));
        dest = add(dest, mul((0.5f - xratio) * camera.screen_width, camera.right_direction));
        dest = add(dest, mul((0.5f - yratio) * camera.screen_height, camera.up_direction));

        r.direction = Normalize(sub(dest, camera.eye));
        return r;
    }

}
