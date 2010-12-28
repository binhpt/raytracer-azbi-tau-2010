package AZBIrenderer;

import AZBIrenderer.Vector3.Point3d;
import java.util.LinkedList;

/**
 * Monte Carlo lighting.
 * @author Barak Itkin
 */
public class LightAreaMc extends Light {

    ThreadLocal<LinkedList<Double>> distances = new ThreadLocal<LinkedList<Double>>();
    public LightAreaMc()
    {
        super();
        this.candidates = 9;
        this.radius = 2;
    }

    /**
     * Assumption: will be called once for each point, and by the order of rays
     * as returned by getrays
     */
    @Override
    public Color EffectFromLight(@Point3d Vector3 point) {
        double d = 1-distances.get().removeFirst();
        return new Color(color.r / (float)d, color.g / (float)d, color.b / (float)d, 1f);
    }

    public void fillMissing() {
        this.color = Color.mul((float)(Math.sqrt(3)*radius)/candidates, this.color);
    }

    public int candidates;
    public @Point3d Vector3 pos;
    public double radius;

    @Override
    public double GetRay(Vector3 point, LinkedList<Ray> rays) {
        LinkedList<Double> Pdistances = distances.get();
        if (Pdistances == null) distances.set(Pdistances = new LinkedList<Double>());
        double t = Double.MIN_VALUE;
        for (int i = 0; i < candidates; i++) {
            Ray ray = new Ray();
            ray.origin = point;
            Vector3 offset = Vector3.Normalize(new Vector3(Math.random()-0.5, Math.random()-0.5, Math.random()-0.5));
            // The chances of 0 0 0, are trully zero;
            double d = radius * Math.random();
            if (Double.isNaN(offset.x)) { offset = new Vector3(); d = 0; }
            Vector3 src = Vector3.add(pos, Vector3.mul(d, offset));
            ray.direction = Vector3.sub(src, point);
            t = Math.max(t, Vector3.Norm(ray.direction));
            ray.direction = Vector3.Normalize(ray.direction);
            rays.add(ray);
            Pdistances.addLast(d);
        }
        return t;
    }


}
