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

    public Render(String config) {
        this.surfaces = new ArrayList<Surface>();
        this.lights = new ArrayList<Light>();
        this.init = false;
        this.config = config;
    }

    private void init() {
    }

    public void render(int screenWidth, int screenHeight) {
        Color pixel;
        Ray r;

        ConfigParser parser = new ConfigParser(this);
        parser.Parse(config);

        render = new BufferedImage(screenWidth, screenHeight, BufferedImage.TYPE_INT_RGB);

	/****ROUGH DRAFT of how it should go:***/
	//additional camera initializations
        if (camera.direction == null && camera.look_at != null)
            camera.direction = sub (camera.look_at, camera.eye);
	camera.right_direction = CrossProduct(camera.up_direction, camera.direction);
	camera.up_direction = CrossProduct(camera.right_direction, camera.direction);
	camera.P1 =
                sub (
                    sub(
                        mul(camera.screen_dist, camera.direction),
                        mul(camera.screen_width / 2, camera.right_direction)
                    ),
                    mul(camera.screen_width / 2, camera.up_direction)
                );

	camera.screen_height = camera.screen_width * (screenWidth / screenHeight);

	//iterate every pixel of the display screen
	//multithreading should go here
        float[] color = new float[4];
	for (int i = 0; i < screenHeight; i++)
		for (int j = 0; j < screenWidth; j++)
		{
			r = CreateRay(i / screenHeight, j / screenWidth);
			//UI related- image[i, j] = ShootRay(ray);
			pixel = ShootRay(r);
                        pixel.fillArray(color);
			render.getRaster().setPixel(j, i, color);
			//1 ray for now, anti aliasing later
		}

    }

    public Color ShootRay(Ray r) {
        Color c;
        IntersectionData closestIntersect = new IntersectionData();
        IntersectionData temp = new IntersectionData();
        closestIntersect.T = -1;//500 - max distance. maybe make macro, maybe get from scene

        for (Surface surf : surfaces) //if there is a collision, and its T is smaller, this is the new closest collision
        {
            if (surf.Intersection(r, temp) && (closestIntersect.T == -1 || temp.T < closestIntersect.T)) {
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
        Vector3 p = new Vector3();
        p = add(
                add(camera.P1,
                    mul(((xratio + 0.5f) * camera.screen_width), camera.right_direction)),
                    mul(((yratio + 0.5f) * camera.screen_width), camera.up_direction));

        r.direction = Normalize(sub(p, camera.eye)); //if we don't need to normalize, remove the normalize
        return r;
    }

}
