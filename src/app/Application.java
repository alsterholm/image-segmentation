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
    private final Filter GAUSSIAN               = new GaussianBlur();
    private final Filter GREYSCALE              = new Greyscale();
    private final Filter SOBEL                  = new Sobel();
    private final Filter THRESHOLD_SEGMENTATION = new ThresholdSegment();

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

    private String filename;
    private String directory;

    private Image image;

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
            case "gaussian":
                f = GAUSSIAN;
                break;

            case "greyscale":
                f = GREYSCALE;
                break;

            case "sobel":
                f = SOBEL;
                break;

            case "threshold":
                f = THRESHOLD_SEGMENTATION;
                break;

            case "edge_thinning":
                f = new EdgeThinning(((Sobel) SOBEL).getAngles());
                break;
        }

        if (f != null) {

            t = System.currentTimeMillis();

            System.out.printf("%-50s", "Applying filter " + f);

            Image i = image.apply(f);
            if (f instanceof ThresholdSegment) {
                ThresholdSegment c = (ThresholdSegment) f;
                c.saveSegments(directory + filename + "-segment");
            }

            i.save(directory + filename + "-" + f.getSuffix());
            t = System.currentTimeMillis() - t;
            System.out.printf("Done (%s ms)\n", t);

            image = i;
        }
    }

    /**
     * Run the program.
     *
     * @param filename Filename
     */
    public void run(String filename) {
        if (!this.validateFilenameExtension(filename))
            return;
        String dir      = System.getProperty("user.dir");
        String file     = dir + File.separator +  filename;
        this.filename   = filename.substring(0, filename.indexOf('.'));
        this.directory  = ("res" + File.separator + this.filename + File.separator);
        String path     = dir + File.separator + "res" + File.separator + this.filename;

        new File(path).mkdirs();

        try {
            image = new Image(file);

            //apply("greyscale");
            //apply("gaussian");
            //apply("sobel");
            //apply("edge_thinning");
            apply("threshold");


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
    private boolean validateFilenameExtension(String filename) {
        int i = filename.indexOf('.');

        if (i == -1) {
            System.out.println("Invalid file format.");
            return false;
        }

        String ending = filename.substring(i);

        if (!Arrays.asList(ALLOWED_FILENAME_EXTENSIONS).contains(ending)) {
            System.out.println("Invalid file ending: " + ending);
            return false;
        }

        return true;
    }
}
