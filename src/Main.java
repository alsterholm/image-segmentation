import app.Application;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {
    public static void main(String[] args) {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        String filename = "";
        String mode = "";
        int threshold, minimumPixels, maximumPixels;

        if (args.length > 0) {
            filename = args[0];
        } else {
            System.out.println("Please enter the name of the file (relative to your current directory):");
            try {
                filename = br.readLine();
                System.out.println();
            } catch (IOException e) {}
        }

        System.out.println(
            "Enter the filter(s) you wish to use (e.g. '12', '14', '2' or '12345' without quotation marks)" +
            "\n\n" +
            "1. Greyscale" + "\n" +
            "2. Gaussian Blur" + "\n" +
            "3. Sobel" + "\n" +
            "4. Edge Thinning" + "\n" +
            "5. Segmentation" + "\n"
        );

        try {
            mode = br.readLine();
            System.out.println();
        } catch (IOException e) {}

        if (mode.contains("5")) {
            System.out.println("Please enter threshold, minimum and maximum pixels in each segment.\n");

            try {
                System.out.print("Threshold: ");
                threshold       = Integer.parseInt(br.readLine());
                System.out.print("Minimum pixels: ");
                minimumPixels   = Integer.parseInt(br.readLine());
                System.out.print("Maximum pixels: ");
                maximumPixels   = Integer.parseInt(br.readLine());
                System.out.println();
                new Application(filename, mode, threshold, minimumPixels, maximumPixels).run();
            } catch (Exception e) {
                System.out.println("ERROR: Bad input, aborting...");
                System.exit(0);
            }
        } else {
            new Application(filename, mode).run();
        }
    }
}