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

    public static boolean shootAtSurfaces (Iterable<? extends Surface> surfaces, Ray r,
            IntersectionData closestIntersect)
    {
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

    public static boolean ShootLightAtSurfaces(List<Surface> surfaces, Ray r, float maxT)
    {
        IntersectionData temp = new IntersectionData();
        for (Surface surf : surfaces)
        {
            //OPTIMIZE:  think of a better way than the > 0.001
            if (surf.Intersection(r, temp) && temp.T < maxT && temp.T > 0.001f)// && !temp.point.equals(r.origin))
                return false;
        }
        return true;
    }

    /* Returns null in case of no intersection */
    public Color ShootRay(Ray r) {
        IntersectionData intersect = new IntersectionData();
        
        IntersectionData lightIntersection = new IntersectionData();
        lightIntersection.T = Float.MAX_VALUE;
        Ray lightray;

        Color color = new Color(this.scene.ambient_light.r, this.scene.ambient_light.g, this.scene.ambient_light.b, 1);
        Color tc;

        double ambient, specular1, specular2;
        Vector3 H;

        if (shootAtSurfaces(surfaces, r, intersect))
        {

            /*
             * not done making it work, just going to sleep
             */
            for (Light light : lights)
            {
                //OPTIMIZE
                lightray = light.GetRay(intersect.point);
                //lightray.origin = add(lightray.origin, mul(Float.MIN_VALUE, lightray.direction));
                //float.maxvalue is only for LightDirected, change later
                if (ShootLightAtSurfaces(surfaces, lightray, Float.MAX_VALUE))
                //if (lightIntersection.point.equals(closestIntersect.point))
                {
                    //just for testing, need to OPTIMIZE.. alot
                    tc = light.EffectFromLight(intersect.point); //I(L) in the presentation

                    //diffuse component: K(d) * NL * I(L)
                    lightray.direction = Normalize(new Vector3(-lightray.direction.x, -lightray.direction.y, -lightray.direction.z));
                    ambient = InnerProduct(intersect.normal, lightray.direction); //N*L
                    if (ambient < 0) ambient = 1f;

                    //specular component: K(s) * (VR)^n *I(L)
                    H = add(r.direction, lightray.direction);
                    specular1 = InnerProduct(H, intersect.normal);
                    if (specular1 < 0) specular1 = 0;
                    specular2 = Math.pow(specular1, intersect.surface.mtl_shininess);

                    color.r += tc.r * (intersect.surface.mtl_diffuse.r * ambient + specular2 * intersect.surface.mtl_specular.r);
                    color.g += tc.g * (intersect.surface.mtl_diffuse.g * ambient + specular2 * intersect.surface.mtl_specular.g);
                    color.b += tc.b * (intersect.surface.mtl_diffuse.b * ambient + specular2 * intersect.surface.mtl_specular.b);

                }
                //float light = Math.abs(Vector3.InnerProduct(normal, LightGlobal));
                //return new Color(sf.mtl_diffuse.r * light, sf.mtl_diffuse.g * light, sf.mtl_diffuse.b * light, 1);
            }
            
            return color;// closestIntersect.surface.mtl_diffuse; //flat color
        }
        else
            return null;
    }


}
