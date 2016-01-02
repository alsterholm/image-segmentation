package transformations;

import utilities.Image;

/**
 * Created by andreas on 2016-01-02.
 */
public class Gauss {
    public static final int[][] G = {
        {1, 4, 7, 4, 1},
        {4, 16, 26, 16, 4},
        {7, 26, 41, 26, 7},
        {4, 16, 26, 16, 4},
        {1, 4, 7, 4, 1},
    };

    public static final int[][] H = {
        {2, 4, 5, 4, 2},
        {4, 9, 12, 9, 4},
        {5, 12, 15, 12, 5},
        {4, 9, 12, 9, 4},
        {2, 4, 5, 4, 2}
    };

    public static final int[][] J = {
        {1, 1, 1},
        {1, 10, 1},
        {1, 1, 1},
    };

    public static Image run(Image input) {
        Image output = Image.convolve(H, input);

        return output;
    }
}
