package transformations;

import utilities.Image;
import utilities.Pixel;

/**
 * Created by andreas on 2016-01-02.
 */
public class ThresholdSegmentation {
    public static Image segment(Image input) {
        int W = input.getWidth();
        int H = input.getHeight();

        Image output = new Image(W, H);
        double threshold = calculateThreshold(input);

        for (int x = 0; x < W; x++) {
            for (int y = 0; y < H; y++) {
                double i = input.getPixel(x, y).getIntensity();

                if (i < threshold) {
                    output.getPixel(x, y).setARGB(255, 255, 255, 255);
                } else {
                    output.getPixel(x, y).setARGB(255, 0, 0, 0);
                }
            }
        }

        return output;
    }


    private static double calculateThreshold(Image input) {
        int W = input.getWidth();
        int H = input.getHeight();

        double u1, u2, tOld = 0, tNew = 0;

        Pixel p1 = input.getPixel(0, 0);
        Pixel p2 = input.getPixel(W - 1, 0);
        Pixel p3 = input.getPixel(0, H - 1);
        Pixel p4 = input.getPixel(W - 1, H - 1);


        u1 = (p1.getIntensity() + p2.getIntensity() + p3.getIntensity() + p4.getIntensity()) / 4;
        u2 = intensityWithoutCorners(input);

        tNew = (u1 + u2) / 2;

        while (tNew != tOld) {
            int u1c = 0, u2c = 0;
            u1 = 0;
            u2 = 0;

            for (int x = 0; x < W; x++) {
                for (int y = 0; y < H; y++) {
                    double i = input.getPixel(x, y).getIntensity();

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

    private static double intensityWithoutCorners(Image input) {
        int W = input.getWidth();
        int H = input.getHeight();

        double avg = 0;
        int pxAmount = (W * H) - 4;

        for (int x = 0; x < W; x++) {
            for (int y = 0; y < H; y++) {
                if (!((x == 0 && y == 0) || (x == W - 1 && y == 0) || (x == 0 && y == H - 1) || (x == W - 1 && y == H - 1))) {
                    avg += input.getPixel(x, y).getIntensity();
                }
            }
        }

        return avg / pxAmount;
    }
}
