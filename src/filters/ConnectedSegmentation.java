package filters;

import entities.Image;
import entities.Pixel;
import interfaces.Filter;

import java.util.LinkedList;
import java.util.Random;
import java.util.Stack;

/**
 * Created by andreas on 2016-01-04.
 */
public class ConnectedSegmentation implements Filter {
    private LinkedList<LinkedList<Pixel>> segments = new LinkedList<>();

    private final int THRESHOLD = 10;
    private boolean[][] visited;

    private int W;
    private int H;

    private final int[][] DIRECTIONS = new int[][] {
            {-1,  0 },
            { 0, -1 },
            { 1,  0 },
            { 0,  1 }
    };

    @Override
    public Image apply(Image image) {
        W = image.getWidth();
        H = image.getHeight();

        visited = new boolean[W][H];

        for (int x = 0; x < W; x++) {
            for (int y = 0; y < H; y++) {
                Pixel p = image.getPixel(x, y);
                if (!visited[x][y]) {
                    LinkedList<Pixel> segment = new LinkedList<>();
                    segment.add(p);
                    segments.add(segment);
                    dfs(x, y, p, segment, image);
                }
            }
        }

        return image;
    }

    private void dfs(int x, int y, Pixel p, LinkedList<Pixel> segment, Image image) {
        if (visited[x][y])
            return;

        for (int[] direction : DIRECTIONS) {
            Pixel n = image.getPixel(x + direction[0], y + direction[1]);

            if (n != null) {
                if (compareColors(p, n) < THRESHOLD) {
                    visited[x][y] = true;
                    segment.add(n);
                    dfs(x + direction[0], y + direction[1], n, segment, image);
                }
            }
        }
    }

    public double compareColors(Pixel p1, Pixel p2) {
        int a = (int) p1.getIntensity();
        int b = (int) p2.getIntensity();

        return Math.abs(a - b);
    }

    public LinkedList<LinkedList<Pixel>> getSegments() {
        return segments;
    }

    public void saveSegments(String filename) {

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
