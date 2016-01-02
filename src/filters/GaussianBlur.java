package filters;

import interfaces.Filter;
import entities.Image;

public class GaussianBlur implements Filter {
    public static final int[][] H1 = {
        { 1,  4,  7,  4, 1 },
        { 4, 16, 26, 16, 4 },
        { 7, 26, 41, 26, 7 },
        { 4, 16, 26, 16, 4 },
        { 1,  4,  7,  4, 1 },
    };

    public static final int[][] H2 = {
        { 2,  4,  5,  4, 2 },
        { 4,  9, 12,  9, 4 },
        { 5, 12, 15, 12, 5 },
        { 4,  9, 12,  9, 4 },
        { 2,  4,  5,  4, 2 }
    };

    public static final int[][] H3 = {
        {1, 1, 1},
        {1, 10, 1},
        {1, 1, 1},
    };

    @Override
    public Image apply(Image image) {
        return new Convolution(H2).apply(image);
    }

    @Override
    public String getSuffix() {
        return "gaussian";
    }

    @Override
    public String toString() {
        return "Gaussian Blur";
    }
}
