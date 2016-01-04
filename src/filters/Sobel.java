package filters;

import interfaces.Filter;
import entities.Image;
import entities.Pixel;


/**
 * Class used for applying a Sobel filter
 * to an image.
 *
 * @author Jimmy Lindström (ae7220)
 * @author Andreas Indal (ae2922)
 */
public class Sobel implements Filter {
    public static final int[][] HX = {
        { -1,  0,  1 },
        { -2,  0,  2 },
        { -1,  0,  1 }
    };

    public static final int[][] HY = {
        { -1, -2, -1 },
        {  0,  0,  0 },
        {  1,  2,  1 }
    };

    private double[][] angles;

    @Override
    public Image apply(Image image) {
        int W = image.getWidth();
        int H = image.getHeight();

        Image output = new Image(W, H);

        Image imageX = image.apply(new Convolution(HX));
        Image imageY = image.apply(new Convolution(HY));

        angles = new double[W][H];

        for (int y = 0; y < H; y++) {
            for (int x = 0; x < W; x++) {
                Pixel px = imageX.getPixel(x, y);
                Pixel py = imageY.getPixel(x, y);

                int r = (int) Math.sqrt(px.r() * px.r() + py.r() * py.r());
                int g = (int) Math.sqrt(px.g() * px.g() + py.g() * py.g());
                int b = (int) Math.sqrt(px.b() * px.b() + py.b() * py.b());

                angles[x][y] = Math.atan2(py.r(), px.r());
                output.getPixel(x, y).setRGB(r, g, b);
            }
        }
        return output;
    }

    public double[][] getAngles() {
        return this.angles;
    }

    @Override
    public String getSuffix() {
        return "sobel";
    }

    @Override
    public String toString() {
        return "Sobel";
    }
}
