package entities;

import interfaces.Filter;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.stream.Stream;

public class Image {
    private Pixel[][] pixels;

    public Image(String filePath) throws IOException {
        this.pixels = load(ImageIO.read(new File(filePath)));
    }

    public Image(Pixel[][] pixels) {
        this.pixels = pixels;
    }

    public Image(int width, int height) {
        this.pixels = new Pixel[height][width];

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                pixels[i][j] = new Pixel();
            }
        }
    }

    public Stream<Pixel> stream() {
        return Arrays.stream(this.pixels).flatMap(x -> Arrays.stream(x));
    }

    public int getWidth() {
        return pixels[0].length;
    }

    public int getHeight() {
        return pixels.length;
    }

    public Pixel getPixel(int x, int y) {
        if (!(x < 0 || x >= getWidth() || y < 0 || y >= getHeight())) {
            return pixels[y][x];
        }
        return null;
    }

    public void save(String fileName) {
        try {
            BufferedImage bufferedImage = this.getBufferedImage();
            ImageIO.write(bufferedImage, "png", new File(fileName + ".png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Pixel[][] load(BufferedImage image) {
        byte[] pixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
        int W = image.getWidth();
        int H = image.getHeight();
        boolean hasAlphaChannel = image.getAlphaRaster() != null;

        Pixel[][] img = new Pixel[H][W];

        int pixelLength = hasAlphaChannel ? 4 : 3;

        for (int p = 0, x = 0, y = 0; p < pixels.length; p += pixelLength) {
            int argb = 0;
            argb += hasAlphaChannel ? ((int) pixels[p] & 0xff) << 24 : -16777216;
            argb += ((int) pixels[p + (hasAlphaChannel ? 3 : 2)] & 0xff) << 16;
            argb += ((int) pixels[p + (hasAlphaChannel ? 2 : 1)] & 0xff) << 8;
            argb +=  (int) pixels[p + (hasAlphaChannel ? 1 : 0)] & 0xff;

            img[x][y] = new Pixel(argb);
            y++;

            if (y == W) {
                y = 0;
                x++;
            }
        }

        return img;
    }

    public Image apply(Filter filter) {
        return filter.apply(this);
    }

    public BufferedImage getBufferedImage() {
        int height = pixels.length;
        int width = pixels[0].length;

        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                bufferedImage.setRGB(x, y, pixels[y][x].getARGB());
            }
        }
        return bufferedImage;
    }
}