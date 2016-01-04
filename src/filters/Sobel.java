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

    @Override
    public Image apply(Image image) {
        int W = image.getWidth();
        int H = image.getHeight();

        Image output = new Image(W, H);

        Image imageX = image.apply(new Convolution(HX));
        Image imageY = image.apply(new Convolution(HY));

        for (int y = 0; y < H; y++) {
            for (int x = 0; x < W; x++) {
                Pixel p1 = imageX.getPixel(x, y);
                Pixel p2 = imageY.getPixel(x, y);

                int r = (int) Math.sqrt(p1.r() * p1.r() + p2.r() * p2.r());
                int g = (int) Math.sqrt(p1.g() * p1.g() + p2.g() * p2.g());
                int b = (int) Math.sqrt(p1.b() * p1.b() + p2.b() * p2.b());

                output.getPixel(x, y).setRGB(r, g, b);
            }
        }
        return output;
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
