package filters;

import interfaces.Filter;
import entities.Image;
import entities.Pixel;

public class Convolution implements Filter {
    private int[][] kernel;

    public Convolution(int[][] kernel) {
        this.kernel = kernel;
    }

    @Override
    public Image apply(Image image) {
        int W = image.getWidth();
        int H = image.getHeight();
        int M = -kernel.length / 2;
        int N = kernel.length / 2;
        int sum = 0;

        for (int[] r : kernel)
            for (int c : r)
                sum += c;

        sum = sum < 1 ? 1 : sum;

        Image out = new Image(W, H);

        for (int x = 0; x < W; x++) {
            for (int y = 0; y < H; y++) {
                double value[] = new double[] {0, 0, 0};

                for (int kx = M; kx <= N; kx++) {
                    for (int ky = M; ky <= N; ky++) {
                        Pixel currentPixel = image.getPixel(x - kx, y - ky);
                        if (currentPixel != null) {
                            value[0] += kernel[kx - M][ky - M] * currentPixel.getR();
                            value[1] += kernel[kx - M][ky - M] * currentPixel.getG();
                            value[2] += kernel[kx - M][ky - M] * currentPixel.getB();
                        }
                    }
                }

                Pixel core = out.getPixel(x, y);
                core.setRGB((int) value[0] / sum, (int) value[1] / sum, (int) value[2] / sum);
            }
        }

        return out;
    }

    @Override
    public String getSuffix() {
        return "convolution";
    }

    @Override
    public String toString() {
        return "Convolution";
    }
}
