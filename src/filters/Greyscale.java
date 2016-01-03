package filters;

import entities.Image;
import entities.Pixel;
import interfaces.Filter;

/**
 * Class used for applying a greyscale filter
 * to an image.
 *
 * @author Jimmy Lindström (ae7220)
 * @author Andreas Indal (ae2922)
 */
public class Greyscale implements Filter {
    @Override
    public Image apply(Image image) {
        int W = image.getWidth();
        int H = image.getHeight();

        Image output = new Image(W, H);

        for (int y = 0; y < H; y++) {
            for (int x = 0; x < W; x++) {
                // Set all channels of each pixel to the average
                // of the R, G and B channels to create a greyscale
                // value.
                Pixel p = image.getPixel(x, y);
                int   g = (int) p.getIntensity();

                output.getPixel(x, y).setRGB(g);
            }
        }

        return output;
    }

    @Override
    public String getSuffix() {
        return "greyscale";
    }

    @Override
    public String toString() {
        return "Greyscale";
    }
}
