
package AZBIrenderer;

import AZBIrenderer.Mesh.RawMesh;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A class containing methods for parsing OFF and PLY mesh files
 * @author Barak Itkin
 */
public class MeshParser {

    public static class ParsingException extends Exception {

        public ParsingException(String message) {
            super(message);
        }
    }

    /**
     * The longest prefix required to read from a mesh file, to distinguish it
     * between the supported formats
     */
    public static final int longestFormatName = 3;

    /**
     * Read a list of space seperated floats from an input line, while skipping
     * a given number of "words" (A "word" is any sequence of non-space characters)
     */
    public static void readLine(String line, float[] dest, int skipCount) {
        String[] nums = line.replaceAll("\\s+", " ").trim().split(" ");
        for (int i = skipCount, j = 0; i < nums.length; i++, j++) {
            dest[j] = Float.parseFloat(nums[i]);
        }
    }

    /**
     * Read a list of space seperated ints from an input line, while skipping
     * a given number of "words" (A "word" is any sequence of non-space characters)
     */
    public static void readLine(String line, int[] dest, int skipCount) {
        String[] nums = line.replaceAll("\\s+", " ").trim().split(" ");
        for (int i = skipCount, j = 0; i < nums.length; i++, j++) {
            dest[j] = Integer.parseInt(nums[i]);
        }
    }

    /*              n           m        0
     * 1 X { OFF numVertices numFaces numEdges
     *     / x1 y1 z1
     * n X |   ....
     *     \ xn yn zn
     *     / 3 v11 v12 v13
     * m X | 3    ....
     *     \ 3 vm1 vm2 vm3
     */
    public static RawMesh parseOFF(FileInputStream in) throws ParsingException {
        float[] input = new float[3];
        int[] inputW = new int[3];
        int n, m;
        RawMesh mesh;

        Scanner inStream = new Scanner(in);
        String line = inStream.nextLine();

        if (line == null || !line.trim().equals("OFF")) {
            throw new ParsingException("The OFF file should begin with a \"OFF\" in "
                    + "it's first line!");
        }

        if ((line = inStream.nextLine()) == null) {
            throw new ParsingException("Missing OFF file header!");
        }

        readLine(line, inputW, 0);
        n = (int) inputW[0];
        m = (int) inputW[1];

        if (n < 0 || m < 0) {
            throw new ParsingException("Missing OFF file header!");
        }

        mesh = new RawMesh(n, m);

        for (int i = 0; i < n; i++) {
            if ((line = inStream.nextLine()) == null) {
                throw new ParsingException("Not enough vertices in OFF file!");
            }
            readLine(line, input, 0);
            mesh.vertices[i] = new Point3(input);
        }

        for (int i = 0; i < m; i++) {
            if ((line = inStream.nextLine()) == null) {
                throw new ParsingException("Not enough faces in OFF file!");
            }
            readLine(line, inputW, 1);
            if (!(line = line.trim()).startsWith("3")) {
                throw new ParsingException("Only triangular meshes are supported!");
            }
            System.arraycopy(inputW, 0, mesh.faces[i], 0, 3);
        }

        return mesh;
    }

    /*           n
     * 1 X { numVertices
     *           m
     * 1 X { numFaces
     *     / x1 y1 z1
     * n X |   ....
     *     \ xn yn zn
     *     / 3 v11 v12 v13
     * m X | 3    ....
     *     \ 3 vm1 vm2 vm3
     */
    public static RawMesh parsePLY(FileInputStream in) throws ParsingException {
        float[] input = new float[3];
        int[] inputW = new int[3];
        int n, m;
        RawMesh mesh;

        Scanner inStream = new Scanner(in);
        String line = inStream.nextLine();

        if ((line = inStream.nextLine()) == null) {
            throw new ParsingException("Missing PLY file header!");
        }

        readLine(line, inputW, 0);
        n = inputW[0];

        if ((line = inStream.nextLine()) == null) {
            throw new ParsingException("Missing PLY file header!");
        }

        readLine(line, inputW, 0);
        m = inputW[0];

        mesh = new RawMesh(n, m);

        for (int i = 0; i < n; i++) {
            if ((line = inStream.nextLine()) == null) {
                throw new ParsingException("Not enough vertices in PLY file!");
            }
            readLine(line, input, 0);
            mesh.vertices[i] = new Point3(input);
        }

        for (int i = 0; i < m; i++) {
            if ((line = inStream.nextLine()) == null) {
                throw new ParsingException("Not enough faces in PLY file!");
            }
            readLine(line, inputW, 1);
            if (!(line = line.trim()).startsWith("3")) {
                throw new ParsingException("PLY face line should start with 3!");
            }
            System.arraycopy(inputW, 0, mesh.faces[i], 0, 3);
        }

        return mesh;
    }

    /**
     * Parse a mesh file (either a PLY or an OFF file)
     */
    public static RawMesh parse(String filename) {
        RawMesh mesh = null;
        FileInputStream input = null;
        try {
            byte[] format = new byte[longestFormatName];

            input = new FileInputStream(filename);

            if (input.read(format) != 3) {
                throw new ParsingException("Mesh file is too short to be parsed... ");
            }

            input.close();
            input = null;

            input = new FileInputStream(filename);
            if (new String(format, Charset.forName("ASCII")).equals("OFF")) {
                mesh = parseOFF(input);
            } else {
                mesh = parsePLY(input);
            }
        } catch (NumberFormatException ex) {
            System.err.println("A parsing exception occured while parsing " + filename);
            System.err.println(ex.getMessage());
        } catch (ParsingException ex) {
            System.err.println("A parsing exception occured while parsing " + filename);
            System.err.println(ex.getMessage());
        } catch (FileNotFoundException ex) {
            System.err.println("The file " + filename + " was not found...");
        } catch (IOException ex) {
            System.err.println("An IO exception occured while reading " + filename);
            System.err.println(ex.getMessage());
        } finally {
            try {
                if (input != null) {
                    input.close();
                }
            } catch (IOException ex) {
                Logger.getLogger(MeshParser.class.getName()).log(Level.SEVERE, null, ex);
            }
            return mesh;
        }
    }
}
