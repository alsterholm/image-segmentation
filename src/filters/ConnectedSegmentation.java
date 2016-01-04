package filters;

import entities.Image;
import entities.Pixel;
import interfaces.Filter;

import java.util.LinkedList;

/**
 * Created by andreas on 2016-01-04.
 */
public class ConnectedSegmentation implements Filter {
    private LinkedList<Image> segments = new LinkedList<>();

    private final int THRESHOLD = 150;

    private final int[][] DIRECTIONS = new int[][] {
            {-1,  0 },
            { 0, -1 },
            { 1,  0 },
            { 0,  1 }
    };

    @Override
    public Image apply(Image image) {
        int W = image.getWidth();
        int H = image.getHeight();

        for (int x = 0; x < W; x++) {
            for (int y = 0; y < H; y++) {
                Pixel p = image.getPixel(x, y);
                if (!p.isVisited()) {
                    Image segment = new Image(W, H);

                    segment.getPixel(x, y).setRGB(255);
                    segments.add(segment);
                    dfs(x, y, p, segment, image);
                }
            }
        }

        return image;
    }

    public void dfs(int x, int y, Pixel p, Image segment, Image image) {
        if (!p.isVisited()) {
            p.setVisited();

            for (int[] direction : DIRECTIONS) {
                Pixel n = image.getPixel(x + direction[0], y + direction[1]);
                if (n != null) {
                    if (compareColors(p, n) < THRESHOLD) {
                        segment.getPixel(x, y).setRGB(255);
                        dfs(x + direction[0], y + direction[1], n, segment, image);
                    }
                }
            }
        }
    }

    public double compareColors(Pixel p1, Pixel p2) {
        int r = p2.r() - p1.r();
        int g = p2.g() - p1.g();
        int b = p2.b() - p1.b();
        return Math.sqrt(r * r + b * b + b * b);
    }

    public LinkedList<Image> getSegments() {
        return segments;
    }

    public void saveSegments(String filename) {
        int x = 1;
        for (Image i : segments) {
            i.save(filename  + "-" + x);
            x++;
        }
    }

    @Override
    public String getSuffix() {
        return "connectedsegment";
    }

    @Override
    public String toString() {
        return "Connected Segmentation";
    }
}
