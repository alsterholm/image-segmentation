package filters;

import interfaces.Filter;
import entities.Image;
import entities.Pixel;

/**
 * Class used for convolving images with a supplied
 * kernel.
 *
 * @author Jimmy Lindström (ae7220)
 * @author Andreas Indal (ae2922)
 */
public class Convolution implements Filter {
    private int[][] kernel;

    /**
     * Construct a new Convolution object with
     * the kernel to use.
     *
     * @param kernel Kernel
     */
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

        Image output = new Image(W, H);

        for (int x = 0; x < W; x++) {
            for (int y = 0; y < H; y++) {
                double value[] = new double[] { 0, 0, 0 };

                for (int kx = M; kx <= N; kx++) {
                    for (int ky = M; ky <= N; ky++) {
                        Pixel p = image.getPixel(x - kx, y - ky);
                        if (p != null) {
                            value[0] += kernel[kx - M][ky - M] * p.r();
                            value[1] += kernel[kx - M][ky - M] * p.g();
                            value[2] += kernel[kx - M][ky - M] * p.b();
                        }
                    }
                }

                int R = (int) value[0] / sum,
                    G = (int) value[1] / sum,
                    B = (int) value[2] / sum;

                output.getPixel(x, y).setRGB(R, G, B);
            }
        }

        return output;
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
