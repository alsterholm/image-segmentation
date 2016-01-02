package utilities;

import interfaces.Transformation;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.io.IOException;

public class Image {
    private Pixel[][] pixels;

    public Image(String filePath) throws IOException {
        BufferedImage bufferedImage;
        bufferedImage = ImageIO.read(new File(filePath));
        pixels = load(bufferedImage);
    }
    public Image(Pixel[][] pixels) {
        this.pixels = pixels;
    }

    public Image(int width, int height) {
        pixels = new Pixel[height][width];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                pixels[i][j] = new Pixel();
            }
        }
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

    public Image toGrayScale() {
        int W = this.getWidth();
        int H = this.getHeight();

        Image output = new Image(W, H);

        for (int y = 0; y < H; y++) {
            for (int x = 0; x < W; x++) {
                Pixel p = this.getPixel(x, y);
                int g = (p.getR() + p.getG() + p.getB()) / 3;
                output.getPixel(x, y).setARGB(255, g, g, g);
            }
        }

        return output;
    }

    public void save(String fileName) {
        try {
            BufferedImage bufferedImage = getBufferedImage();
            ImageIO.write(bufferedImage, "png", new File(fileName + ".png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Pixel[][] load(BufferedImage image) {
        final byte[] pixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
        final int width = image.getWidth();
        final int height = image.getHeight();
        final boolean hasAlphaChannel = image.getAlphaRaster() != null;

        Pixel[][] img = new Pixel[height][width];

        if (hasAlphaChannel){
            final int pixelLength = 4;
            for (int pixel = 0, row = 0, col = 0; pixel < pixels.length; pixel += pixelLength) {
                int argb = 0;
                argb += (((int) pixels[pixel] & 0xff) << 24); // alpha
                argb += ((int) pixels[pixel + 1] & 0xff); // blue
                argb += (((int) pixels[pixel + 2] & 0xff) << 8); // green
                argb += (((int) pixels[pixel + 3] & 0xff) << 16); // red
                img[row][col] = new Pixel(argb);
                col++;
                if (col == width) {
                    col = 0;
                    row++;
                }
            }
        } else {
            final int pixelLength = 3;
            for (int pixel = 0, row = 0, col = 0; pixel < pixels.length; pixel += pixelLength) {
                int argb = 0;
                argb += -16777216; // 255 alpha
                argb += ((int) pixels[pixel] & 0xff); // blue
                argb += (((int) pixels[pixel + 1] & 0xff) << 8); // green
                argb += (((int) pixels[pixel + 2] & 0xff) << 16); // red
                img[row][col] = new Pixel(argb);
                col++;
                if (col == width) {
                    col = 0;
                    row++;
                }
            }
        }
        return img;
    }

    public static Image convolve(int[][] kernel, Image image) {
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
                core.setARGB(255, (int) value[0] / sum, (int) value[1] / sum, (int) value[2] / sum);
            }
        }

        return out;
    }

    public Image apply(Transformation t) {
        Image output = t.transform(this);
        return output;
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

    public Pixel[][] getPixels() {
        return pixels;
    }
}