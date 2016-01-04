package filters;

import entities.Image;
import interfaces.Filter;

/**
 * Created by andreas on 2016-01-04.
 */
public class Canny implements Filter {
    private final int THRESHOLD_LOW, THRESHOLD_HIGH;

    public Canny(final int THRESHOLD_LOW, final int THRESHOLD_HIGH) {
        this.THRESHOLD_LOW  = THRESHOLD_LOW;
        this.THRESHOLD_HIGH = THRESHOLD_HIGH;
    }

    public Canny() {
        this(14, 50);
    }

    @Override
    public Image apply(Image image) {
        int W = image.getWidth();
        int H = image.getHeight();

        Sobel sobel = new Sobel();

        image = image.apply(new Greyscale());
        image = image.apply(new GaussianBlur());
        image = image.apply(sobel);
        image = image.apply(new EdgeThinning(sobel.getAngles()));
        //image = image.apply(new Hysteresis(THRESHOLD_LOW, THRESHOLD_HIGH));

        return image;
    }

    @Override
    public String getSuffix() {
        return "canny";
    }

    @Override
    public String toString() {
        return "Canny";
    }
}
