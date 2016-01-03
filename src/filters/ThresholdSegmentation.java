package filters;

import interfaces.Filter;
import entities.Image;
import entities.Pixel;

/**
 * Class used for segmenting an image using
 * a simple threshold.
 *
 * @author Jimmy Lindström (ae7220)
 * @author Andreas Indal (ae2922)
 */
public class ThresholdSegmentation implements Filter {
    @Override
    public Image apply(Image image) {
        int W = image.getWidth();
        int H = image.getHeight();

        Image output = new Image(W, H);
        double threshold = this.getThreshold(image);

        for (int x = 0; x < W; x++) {
            for (int y = 0; y < H; y++) {
                double i = image.getPixel(x, y).getIntensity();

                if (i < threshold) {
                    output.getPixel(x, y).setRGB(255, 255, 255);
                } else {
                    output.getPixel(x, y).setRGB(0, 0, 0);
                }
            }
        }

        return output;
    }

    /**
     * Calculate threshold of the image.
     *
     * @param image Image
     * @return Threshold
     */
    private double getThreshold(Image image) {
        int W = image.getWidth();
        int H = image.getHeight();

        double u1, u2, tNew, tOld = 0;

        Pixel p1 = image.getPixel(0, 0);
        Pixel p2 = image.getPixel(W - 1, 0);
        Pixel p3 = image.getPixel(0, H - 1);
        Pixel p4 = image.getPixel(W - 1, H - 1);

        u1 = (p1.getIntensity() + p2.getIntensity() + p3.getIntensity() + p4.getIntensity()) / 4;
        u2 = this.intensityWithoutCorners(image);

        tNew = (u1 + u2) / 2;

        while (tNew != tOld) {
            int u1c = 0, u2c = 0;
            u1 = 0;
            u2 = 0;

            for (int x = 0; x < W; x++) {
                for (int y = 0; y < H; y++) {
                    double i = image.getPixel(x, y).getIntensity();

                    if (i < tNew) {
                        u1 += i;
                        u1c++;
                    } else {
                        u2 += i;
                        u2c++;
                    }
                }
            }

            u1c = u1c == 0 ? 1 : u1c;
            u2c = u2c == 0 ? 1 : u2c;

            u1 = u1 / u1c;
            u2 = u2 / u2c;

            tOld = tNew;
            tNew = (u1 + u2) / 2;
        }

        return tNew;
    }

    /**
     * Get the average intensity excluding the image’s corners.
     *
     * @param image Image
     * @return Average intensity
     */
    private double intensityWithoutCorners(Image image) {
        int W = image.getWidth();
        int H = image.getHeight();

        double avg = 0;
        int pixels = (W * H) - 4;

        for (int x = 0; x < W; x++) {
            for (int y = 0; y < H; y++) {
                if (!(x == 0 && y == 0) && !(x == W - 1 && y == 0) && !(x == 0 && y == H - 1) && !(x == W - 1 && y == H - 1)) {
                    avg += image.getPixel(x, y).getIntensity();
                }
            }
        }

        return avg / pixels;
    }

    public String getSuffix() {
        return "segmentation";
    }

    public String toString() {
        return "Threshold Segmentation";
    }
}
