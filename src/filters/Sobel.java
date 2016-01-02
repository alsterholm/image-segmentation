package filters;

import interfaces.Filter;
import entities.Image;
import entities.Pixel;

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

    public static final int THRESHOLD = 40;

    @Override
    public Image apply(Image image) {
        int W = image.getWidth();
        int H = image.getHeight();

        Image output = new Image(W, H);


        Image imageX = new Convolution(HX).apply(image);
        Image imageY = new Convolution(HY).apply(image);

        for (int y = 0; y < H; y++) {
            for (int x = 0; x < W; x++) {
                Pixel p1 = imageX.getPixel(x, y);
                Pixel p2 = imageY.getPixel(x, y);

                int r = (int) Math.sqrt(p1.getR() * p1.getR() + p2.getR() * p2.getR());
                int g = (int) Math.sqrt(p1.getG() * p1.getG() + p2.getG() * p2.getG());
                int b = (int) Math.sqrt(p1.getB() * p1.getB() + p2.getB() * p2.getB());

                // Reduce noise.

                r = r >= THRESHOLD ? 255 : 0;
                g = g >= THRESHOLD ? 255 : 0;
                b = b >= THRESHOLD ? 255 : 0;

                //if (r > THRESHOLD && r < 255 - THRESHOLD ) r = r <= 127 ? THRESHOLD : 255 - THRESHOLD;
                //if (g > THRESHOLD && g < 255 - THRESHOLD ) g = g <= 127 ? THRESHOLD : 255 - THRESHOLD;
                //if (b > THRESHOLD && b < 255 - THRESHOLD ) b = b <= 127 ? THRESHOLD : 255 - THRESHOLD;

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
