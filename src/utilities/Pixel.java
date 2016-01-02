package utilities;

/**
 * @author Jimmy Maksymiw & Kalle Bornemark
 */
public class Pixel {
    private int a;
    private int r;
    private int g;
    private int b;
    private boolean visited = false;

    public Pixel(int argb) {
        this.a = (argb >> 24) & 0xFF;
        this.r = (argb >> 16) & 0xFF;
        this.g = (argb >> 8) & 0xFF;
        this.b = argb & 0xFF;
    }

    public Pixel(int a, int r, int g, int b) {
        this.a = a;
        this.r = r;
        this.g = g;
        this.b = b;
    }

    public Pixel() {
        this(255, 0, 0, 0);
    }

    public int getA() {
        return a;
    }

    public int getR() {
        return r;
    }

    public int getG() {
        return g;
    }

    public int getB() {
        return b;
    }

    public int getARGB() {
        int argb = 0;

        argb += (a & 0xFF) << 24;
        argb += (r & 0xFF) << 16;
        argb += (g & 0xFF) << 8;
        argb += b & 0xFF;

        return argb;
    }

    public void setARGB(int argb) {
        this.a = (argb >> 24) & 0xFF;
        this.r = (argb >> 16) & 0xFF;
        this.g = (argb >> 8) & 0xFF;
        this.b = argb & 0xFF;
    }

    public void setARGB(int a, int r, int g, int b) {
        this.a = a;
        this.r = r;
        this.g = g;
        this.b = b;
    }

    public boolean isVisited() {
        return visited;
    }

    public void setVisited(boolean visited) {
        this.visited = visited;
    }

    public double getIntensity() {
        return (double) this.r + this.g + this.b / 3;
    }

    @Override
    public String toString() {
        return "Pixel{" +
                ", a=" + a +
                ", r=" + r +
                ", g=" + g +
                ", b=" + b +
                '}';
    }
}