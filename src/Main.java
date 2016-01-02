import transformations.Gauss;
import transformations.Sobel;
import transformations.ThresholdSegmentation;
import utilities.Image;

import java.io.File;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        if (args.length > 0) {
            String dir = System.getProperty("user.dir");
            String filename = args[0];
            String file = dir + File.separator +  filename;
            String fn = filename.substring(0, filename.indexOf('.'));
            String target = ("res" + File.separator + fn + File.separator + fn);

            try {
                long t;

                (new File(dir + File.separator + "res")).mkdir();
                (new File(dir + File.separator + "res" + File.separator + fn)).mkdir();
                Image img = new Image(file);

                /*
                 * Apply Gaussian blur to image and save it.
                 */
                t = System.currentTimeMillis();
                System.out.print("Applying gaussian blur...");
                Image gauss = Gauss.run(img);
                gauss.save(target + "-gauss");
                t = System.currentTimeMillis() - t;
                System.out.printf(" Done (%s ms)\n", t);

                /*
                 * Apply Sobel to original image and save it.
                 */
                t = System.currentTimeMillis();
                System.out.print("Applying sobel...");
                Image sobel = Sobel.run(img);
                sobel.save(target + "-sobel");
                t = System.currentTimeMillis() - t;
                System.out.printf(" Done (%s ms)\n", t);

                /*
                 * Apply Sobel to Gaussian blurred image and save it.
                 */
                t = System.currentTimeMillis();
                System.out.print("Applying sobel on gaussian blur...");
                Image gs = Sobel.run(gauss).toGrayScale();
                gs.save(target + "-gauss-sobel");
                t = System.currentTimeMillis() - t;
                System.out.printf(" Done (%s ms)\n", t);

                /*
                 * Segment shit
                 */
                t = System.currentTimeMillis();
                System.out.print("Applying threshold segmentation...");
                Image seg = ThresholdSegmentation.segment(gs);
                seg.save(target + "-segmentation");
                t = System.currentTimeMillis() - t;
                System.out.printf(" Done (%s ms)\n", t);


            } catch (IOException e) {
                System.err.println("Image could not be loaded.");
            }
        } else {
            System.err.println("Please supply a file name.");
        }
    }
}
