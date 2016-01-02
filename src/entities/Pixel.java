package entities;

public class Pixel {
    private int r;
    private int g;
    private int b;
    private int a;

    public Pixel(int rgba) {
        this.a = (rgba >> 24) & 0xFF;
        this.r = (rgba >> 16) & 0xFF;
        this.g = (rgba >> 8) & 0xFF;
        this.b = rgba & 0xFF;
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
        int rgba = 0;
        
        rgba += (r & 0xFF) << 16;
        rgba += (g & 0xFF) << 8;
        rgba += b & 0xFF;
        rgba += (a & 0xFF) << 24;

        return rgba;
    }

    public void setARGB(int rgba) {
        this.r = (rgba >> 16) & 0xFF;
        this.g = (rgba >> 8) & 0xFF;
        this.b = rgba & 0xFF;

        this.a = (rgba >> 24) & 0xFF;
    }

    public void setARGB(int r, int g, int b, int a) {
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
    }

    public void setRGB(int r, int g, int b) {
        this.setARGB(r, g, b, 255);
    }

    public void setRGB(int i) {
        this.setRGB(i, i, i);
    }

    public double getIntensity() {
        return (double) this.r + this.g + this.b / 3;
    }
}