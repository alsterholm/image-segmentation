package filters;

import entities.Image;
import entities.Pixel;
import interfaces.Filter;

import java.util.LinkedList;

/**
 * Class used for segmenting an image.
 *
 * @author Jimmy Lindström (ae7220)
 * @author Andreas Indal (ae2922)
 */
public class ThresholdSegment implements Filter {
    private final int THRESHOLD;
    private final int MINIMUM_PIXELS;
    private final int MAXIMUM_PIXELS;
    private LinkedList<Pixel> candidates = new LinkedList<>();
    private LinkedList<Image> segments = new LinkedList<>();

    private final int[][] DIRECTIONS = new int[][] {
            {  0,  -1 }, // N
            {  1,  -1 }, // NE
            {  1,   0 }, // E
            {  1,   1 }, // SE
            {  0,   1 }, // S
            { -1,   1 }, // SW
            { -1,   0 }, // W
            { -1,  -1 }, // NW
    };

    /**
     * Construct a new ThresholdSegmentation object.
     *
     * @param THRESHOLD Threshold
     * @param MINIMUM_PIXELS Minimum pixels in each segmentation.
     * @param MAXIMUM_PIXELS Maximum pixels in each segmentation.
     */
    public ThresholdSegment(int THRESHOLD, int MINIMUM_PIXELS, int MAXIMUM_PIXELS) {
        this.THRESHOLD = THRESHOLD;
        this.MINIMUM_PIXELS = MINIMUM_PIXELS;
        this.MAXIMUM_PIXELS = MAXIMUM_PIXELS;
    }

    @Override
    public Image apply(Image image) {
        int W = image.getWidth();
        int H = image.getHeight();

        for (int x = 0; x < W; x++) {
            for (int y = 0; y < H; y++) {
                image.getPixel(x, y).setVisited(false);
            }
        }

        Image output = new Image(W, H);

        for (int x = 0; x < W; x++) {
            for (int y = 0; y < H; y++) {
                Pixel p = image.getPixel(x, y);
                if (!p.isVisited()) {
                    candidates.add(p);
                    findNeighbours(image, output, p);
                }
            }
        }

        return output;
    }

    private void findNeighbours(Image image, Image output, Pixel o) {
        Image segment = new Image(image.getWidth(), image.getHeight());

        // Counter for amount if pixels in the segment
        int pixelCount = 0;
        while (candidates.size() > 0) {
            Pixel p = candidates.removeFirst();

            if (p != null) {
                int px = p.x(), py = p.y();

                // Get original pixel’s color values.
                int R = o.r(),
                    G = o.g(),
                    B = o.b();

                if (!p.isVisited()) {
                    // Determine if the difference is valid
                    boolean diff =  Math.abs(p.r() - R) < THRESHOLD &&
                                    Math.abs(p.b() - G) < THRESHOLD &&
                                    Math.abs(p.g() - B) < THRESHOLD;

                    if (diff) {
                        pixelCount++;

                        // Set colors both of pixels in both the
                        // output image and the segment
                        output.getPixel(px, py).setRGB(p.r(), p.g(), p.b());
                        segment.getPixel(px, py).setRGB(p.r(), p.g(), p.b());

                        p.setVisited(true);

                        for (int[] d : DIRECTIONS)
                            candidates.add(image.getPixel(px + d[0], py + d[1]));
                    }
                }
            }
        }

        if (pixelCount > MINIMUM_PIXELS && pixelCount < MAXIMUM_PIXELS) {
            segments.add(segment);
        }
    }

    /**
     * Save all detected segments to files.
     * @param filename Filename
     */
    public void saveSegments(String filename) {
        int x = 1;
        for (Image image : segments) {
            image.save(filename + "segment-" + x++);
        }
    }

    @Override
    public String getSuffix() {
        return "segm";
    }

    @Override
    public String toString() {
        return "Threshold Segmentation";
    }
}
