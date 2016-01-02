import app.Application;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {
    public static void main(String[] args) {
        String filename = "";
        if (args.length > 0) {
            filename = args[0];
        } else {
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            System.out.println("Please enter the name of the file (relative to your current directory):");
            try {
                filename = br.readLine();
                System.out.println();
            } catch (IOException e) {}
        }

        new Application().run(filename);
    }
}
