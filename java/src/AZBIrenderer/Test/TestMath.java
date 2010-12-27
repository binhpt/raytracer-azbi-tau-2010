/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package AZBIrenderer.Test;

/**
 *
 * @author user
 */
public class TestMath {

    public static void main(String[] args) {

        int c = 1000;
        int count = c * c;
        double[] a = new double[c];
        float[] b = new float[c];

        for (int i = 0; i < c; i++) {
            a[i] = Math.random();
            b[i] = (float) a[i];

        }
        long start, end;

        double tempD;
        start = System.currentTimeMillis();
        for (int i= 0; i < c * c * c; i++) {
            tempD = a[i%c] * a[i%c];
        }
        System.out.println(System.currentTimeMillis() - start);


        double tempF;
        start = System.currentTimeMillis();
        for (int i= 0; i < c * c * c; i++) {
            tempF = b[i % c] * b[i % c];
        }
        System.out.println(System.currentTimeMillis() - start);
    }

}
