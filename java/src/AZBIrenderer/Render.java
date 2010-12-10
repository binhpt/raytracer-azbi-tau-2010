package AZBIrenderer;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import static AZBIrenderer.Vector3.*;

/**
 * This class has all the data needed to produce a render from a config file.
 * The actual rendering happens here!
 * @author Barak Itkin & Adam Zeira
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
        Graphics g;

        ConfigParser parser = new ConfigParser(this);
        parser.Parse(config);

        render = new BufferedImage(screenWidth, screenHeight, BufferedImage.TYPE_INT_ARGB);
        g = render.getGraphics();
        g.setColor(new java.awt.Color(scene.background_col.r, scene.background_col.g, scene.background_col.b));
        g.fillRect(0, 0, screenWidth, screenHeight);;

	/****ROUGH DRAFT of how it should go:***/
	//additional camera initializations

        // Inverted because of the right hand coordinate system
	camera.screen_height = camera.screen_width * (1/(((float)screenWidth) / screenHeight));

	//iterate every pixel of the display screen
	//multithreading should go here
        int color;
	for (int i = 0; i < screenHeight; i++)
		for (int j = 0; j < screenWidth; j++)
		{
			r = camera.CreateRay(((float)i) / screenHeight, ((float)j) / screenWidth);
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


}
