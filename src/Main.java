import java.io.File;

public class Main {
    public static void main(String[] args) {
        if (args.length > 0) {
            String dir = System.getProperty("user.dir");
            String filename = dir + File.separator + args[0];

            // Image img = new Image(filename);
            System.out.println(filename);
        } else {
            System.err.println("Please supply a file name");
        }
    }
}
