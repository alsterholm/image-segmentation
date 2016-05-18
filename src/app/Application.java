package app;

import entities.Image;
import filters.*;
import interfaces.Filter;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

/**
 * Image segmentation application.
 *
 * @author Jimmy Lindström (ae7220)
 * @author Andreas Indal (ae2922)
 */
public class Application {
    private final String[] ALLOWED_FILENAME_EXTENSIONS = {
        ".png", ".gif", ".jpg"
    };

    public static final int[][] HX = {
        { -1,  0,  1 },
        { -2,  0,  2 },
        { -1,  0,  1 }
    };

    public static final int[][] HY = {
        { -1, -2, -1 },
        {  0,  0,  0 },
        {  1,  2,  1 }
    };

    private Image image;
    private String fullFilename, filename, directory, mode;
    private double[][] angles;
    private final int THRESHOLD, MINIMUM_PIXELS, MAXIMUM_PIXELS;
    private boolean validFileExtension;

    public Application(String filename, String mode) {
        this(filename, mode, 50, 300, 8000);
    }

    public Application(String filename, String mode, int THRESHOLD, int MINIMUM_PIXELS, int MAXIMUM_PIXELS) {
        this.validateFilenameExtension(filename);

        this.fullFilename   = filename;
        this.filename       = filename.substring(0, filename.indexOf('.'));
        this.directory      = ("res" + File.separator + this.filename + File.separator);
        this.mode           = mode;
        this.THRESHOLD      = THRESHOLD;
        this.MINIMUM_PIXELS = MINIMUM_PIXELS;
        this.MAXIMUM_PIXELS = MAXIMUM_PIXELS;
    }

    /**
     * Apply a filter to the loaded image, time
     * it and save the new image.
     *
     * @param filter Name of the filter.
     */
    private void apply(String filter) {
        Filter f = null;
        long t;

        switch (filter) {
            case "1":
                f = new Greyscale();
                break;

            case "2":
                f = new GaussianBlur();
                break;

            case "3":
                f = new Sobel();
                break;

            case "4":
                f = new EdgeThinning(this.angles);
                break;

            case "5":
                f = new ThresholdSegment(THRESHOLD, MINIMUM_PIXELS, MAXIMUM_PIXELS);
                break;
        }

        if (f != null) {

            t = System.currentTimeMillis();

            System.out.printf("%-50s", "Applying filter " + f);

            Image i = image.apply(f);
            if (f instanceof ThresholdSegment) {
                ThresholdSegment c = (ThresholdSegment) f;
                c.saveSegments(directory + filename + "-segment");
            } else if (f instanceof Sobel) {
                Sobel s = (Sobel) f;
                this.angles = s.getAngles();
            }

            i.save(directory + filename + "-" + f.getSuffix());
            t = System.currentTimeMillis() - t;
            System.out.printf("Done (%s ms)\n", t);

            image = i;
        }
    }

    /**
     * Run the program.
     */
    public void run() {
        if (!validFileExtension) return;

        String dir  = System.getProperty("user.dir");
        String file = dir + File.separator +  this.fullFilename;
        String path = dir + File.separator + "res" + File.separator + this.filename;

        new File(path).mkdirs();

        try {
            image = new Image(file);

            boolean sobel = false;

            for (char c : this.mode.toCharArray()) {
                if (c == '4' && !sobel) {
                    System.out.println("ERROR: You must run Sobel before Edge Thinning.");
                } else {
                    if (c == '3')
                        sobel = true;
                    apply(String.valueOf(c));
                }
            }

            System.out.printf("\nResults available in %s\n\n", path);
        } catch (IOException e) {
            System.err.println("Image could not be loaded.");
        }

        // Try to open result directory
        try {
            Desktop.getDesktop().open(new File(path));
            System.exit(0);
        } catch (IOException e) {}
    }

    /**
     * Validate the extension of a filename.
     *
     * @param filename Filename
     * @return boolean
     */
    private void validateFilenameExtension(String filename) {
        int i = filename.indexOf('.');

        if (i == -1) {
            System.out.println("Invalid file format.");
            return;
        }

        String ending = filename.substring(i);

        if (!Arrays.asList(ALLOWED_FILENAME_EXTENSIONS).contains(ending)) {
            System.out.println("Invalid file ending: " + ending);
            return;
        }

        validFileExtension = true;
    }
}
