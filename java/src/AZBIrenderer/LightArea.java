
package AZBIrenderer;

/**
 * Implement an area light based on a rectangular uniform grid of point lights
 * @author Barak Itkin
 */
public class LightArea implements ReflectionConstructed, ReflectionWrapper {

    public Color color;
    public @Vector3.Point3d Vector3 p0, p1, p2;
    public double grid_width;

    LightPoint[] lights;

    public Object[] getRealObjects() {
        return lights;
    }

    public void fillMissing() {
        int N = (int) this.grid_width, count = N*N;
        Color part = new Color(color.r/count, color.g/count, color.b/count);

        Vector3 u = Vector3.mul(1.0/(N-1), Vector3.sub(this.p1, this.p0));
        Vector3 v = Vector3.mul(1.0/(N-1), Vector3.sub(this.p2, this.p0));

        this.lights = new LightPoint[N*N];

        Vector3 x = p0;
        for (int i = 0; i < N; i++) {
            Vector3 pos = new Vector3(x);
            for (int j = 0; j < N; j++) {
                this.lights[i*N+j] = new LightPoint();
                this.lights[i*N+j].color = part;
                this.lights[i*N+j].pos = pos;
                pos = Vector3.add(pos, v);
            }
            x = Vector3.add(x, u);
        }
    }

}
