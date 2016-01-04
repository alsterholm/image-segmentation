package filters;

import entities.Image;
import entities.Pixel;
import interfaces.Filter;

/**
 * Class used for thinning edges in images.
 *
 * @author Jimmy Lindström (ae7220)
 * @author Andreas Indal (ae2922)
 */
public class EdgeThinning implements Filter {
    private double[][] angles;

    public EdgeThinning(double[][] angles) {
        this.angles = angles;
    }

    @Override
    public Image apply(Image image) {
        int W = image.getWidth();
        int H = image.getHeight();

        Image output = new Image(W, H);

        for (int x = 0; x < W; x++) {
            for (int y = 0; y < H; y++) {
                double angle = (Math.toDegrees(angles[x][y]) + 180) % 180;

                // Get x and y coordinates of neighbour.
                int nx = angle < 67.5 ? -1 : angle < 112.5 ?  0 : 1;
                int ny = angle < 22.5 ?  0 : angle < 157.5 ? -1 : 0;

                Pixel p     = image.getPixel(x, y);
                Pixel n     = image.getPixel(x + nx, y + ny);
                Pixel n180  = image.getPixel(x - nx, y - ny);

                if (n != null && n180 != null) {
                    if (p.r() >= n.r() && p.r() >= n180.r()) {
                        output.getPixel(x, y).setRGB(p.r(), p.g(), p.b());
                    }
                }
            }
        }

        return output;
    }

    @Override
    public String getSuffix() {
        return "thinned";
    }

    @Override
    public String toString() {
        return "Edge Thinning";
    }
}
