package entities;

/**
 * Class used to represent a pixel of
 * an image.
 *
 * @author Jimmy Lindström (ae7220)
 * @author Andreas Indal (ae2922)
 */
public class Pixel {
    private int a, r, g, b;

    /**
     * Construct a new pixel with the supplied
     * color value.
     *
     * @param argb Color
     */
    public Pixel(int argb) {
        this.a = (argb >> 24) & 0xFF;
        this.r = (argb >> 16) & 0xFF;
        this.g = (argb >> 8)  & 0xFF;
        this.b =  argb        & 0xFF;
    }

    /**
     * Construct a new pixel with the supplied
     * alpha, red, green and blue values.
     *
     * @param a Alpha
     * @param r Red
     * @param g Green
     * @param b Blue
     */
    public Pixel(int a, int r, int g, int b) {
        this.a = a;
        this.r = r;
        this.g = g;
        this.b = b;
    }

    /**
     * Construct a black pixel.
     */
    public Pixel() {
        this(255, 0, 0, 0);
    }

    /**
     * Get the value of the pixel’s alpha channel.
     * @return Alpha
     */
    public int a() {
        return a;
    }

    /**
     * Get the value of the pixel’s red channel.
     * @return Red
     */
    public int r() {
        return r;
    }

    /**
     * Get the value of the pixel’s green channel.
     * @return Green
     */
    public int g() {
        return g;
    }

    /**
     * Get the value of the pixel’s blue channel.
     * @return Blue
     */
    public int b() {
        return b;
    }

    /**
     * Get the 32 bit color value of the pixel.
     * @return Pixel’s color
     */
    public int getARGB() {
        int argb = 0;

        argb += (a & 0xFF) << 24;
        argb += (r & 0xFF) << 16;
        argb += (g & 0xFF) << 8;
        argb +=  b & 0xFF;

        return argb;
    }

    /**
     * Set the color of the pixel.
     *
     * @param a Alpha
     * @param r Red
     * @param g Green
     * @param b Blue
     */
    public void setARGB(int a, int r, int g, int b) {
        this.a = a;
        this.r = r;
        this.g = g;
        this.b = b;
    }

    /**
     * Set the color of a pixel.
     * @param r Red
     * @param g Green
     * @param b Blue
     */
    public void setRGB(int r, int g, int b) {
        this.setARGB(255, r, g, b);
    }

    /**
     * Set a greyscale color to a pixel.
     * @param c Greyscale value
     */
    public void setRGB(int c) {
        this.setRGB(c, c, c);
    }

    /**
     * Get the pixel’s intensity.
     * @return Pixel’s intensity
     */
    public double getIntensity() {
        return (double) (this.r + this.g + this.b) / 3;
    }
}