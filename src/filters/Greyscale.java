package filters;

import entities.Image;
import entities.Pixel;
import interfaces.Filter;

public class Greyscale implements Filter {
    @Override
    public Image apply(Image image) {
        int W = image.getWidth();
        int H = image.getHeight();

        Image output = new Image(W, H);

        for (int y = 0; y < H; y++) {
            for (int x = 0; x < W; x++) {
                Pixel p = image.getPixel(x, y);
                int g = (p.getR() + p.getG() + p.getB()) / 3;
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
