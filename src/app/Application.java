package app;

import entities.Image;
import filters.GaussianBlur;
import filters.Greyscale;
import filters.Sobel;
import filters.ThresholdSegmentation;
import interfaces.Filter;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public class Application {
    private final Filter GAUSSIAN               = new GaussianBlur();
    private final Filter GREYSCALE              = new Greyscale();
    private final Filter SOBEL                  = new Sobel();
    private final Filter THRESHOLD_SEGMENTATION = new ThresholdSegmentation();

    private final String[] ALLOWED_FILE_ENDINGS = {
            ".png", ".gif", ".jpg"
    };

    private String filename;
    private String directory;

    private Image image;

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
        }

        if (f != null) {

            t = System.currentTimeMillis();

            System.out.printf("%-50s", "Applying filter " + f);

            Image i = image.apply(f);
            i.save(directory + filename + "-" + f.getSuffix());
            t = System.currentTimeMillis() - t;
            System.out.printf("Done (%s ms)\n", t);

            image = i;
        }
    }

    public void run(String filename) {
        if (!this.validateFileEnding(filename))
            return;
        String dir      = System.getProperty("user.dir");
        String file     = dir + File.separator +  filename;
        this.filename   = filename.substring(0, filename.indexOf('.'));
        this.directory  = ("res" + File.separator + this.filename + File.separator);
        String path     = dir + File.separator + "res" + File.separator + this.filename;

        new File(path).mkdirs();

        try {
            image = new Image(file);

            apply("greyscale");
            apply("gaussian");
            apply("sobel");
            apply("threshold");

            System.out.printf("\nResults available in %s\n\n", path);
        } catch (IOException e) {
            System.err.println("Image could not be loaded.");
        }
    }

    private boolean validateFileEnding(String filename) {
        String ending = filename.substring(filename.indexOf('.'));

        if (!Arrays.asList(ALLOWED_FILE_ENDINGS).contains(ending)) {
            System.out.println("Invalid file ending: " + ending);
            return false;
        }

        return true;
    }
}
