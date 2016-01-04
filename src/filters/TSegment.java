package filters;

import entities.Image;
import entities.Pixel;
import interfaces.Filter;

import java.util.LinkedList;

public class TSegment implements Filter {
    private final int THRESHOLD = 150;
    private LinkedList<Pixel> candidates = new LinkedList<>();
    private boolean[][] visited;

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

    @Override
    public Image apply(Image image) {
        int W = image.getWidth();
        int H = image.getHeight();

        visited = new boolean[W][H];

        Image output = new Image(W, H);

        for (int y = 0; y < H; y++) {
            for (int x = 0; x < W; x++) {
                if (!visited[x][y]) {
                    System.out.println("x=" + x + ", y=" + y);
                    Pixel p = image.getPixel(x, y);
                    candidates.add(p);
                    findNeighbours(image, output, p);
                }
            }
        }

        return output;
    }

    public void findNeighbours(Image image, Image output, Pixel o) {
        while (candidates.size() > 0) {
            System.out.println("Candidates not empty");
            Pixel p = candidates.removeFirst();

            if (p != null) {
                int px = p.x(), py = p.y();
                System.out.println("Candidate not null (x=" + px + ", y=" + py + ")");

                if (!visited[px][py]) {
                    System.out.println("Candidate not visited");

                    // Get colors from candidate
                    int nr = p.r();
                    int ng = p.b();
                    int nb = p.g();

                    // Calculate differences in RGB values
                    int rDiff = Math.abs(nr - o.r());
                    int gDiff = Math.abs(ng - o.g());
                    int bDiff = Math.abs(nb - o.b());

                    System.out.printf("rdiff=%d,gdiff=%d,bdiff=%d\n", rDiff, gDiff, bDiff);

                    boolean diff = rDiff < THRESHOLD && gDiff < THRESHOLD && bDiff < THRESHOLD;

                    if (diff) {
                        output.getPixel(px, py).setRGB(255);

                        visited[px][py] = true;

                        for (int[] d : DIRECTIONS)
                            candidates.add(image.getPixel(px + d[0], py + d[1]));
                    }
                }
            }
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
