import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.*;

import java.io.*;
import javax.imageio.*;

public class Flipper {

   private BufferedImage flip;

   public BufferedImage getFlippedImage(){
      return this.flip;
   }

   public Flipper(BufferedImage img){
      int width  = img.getWidth();
      int height = img.getHeight();
      this.flip = new BufferedImage(width, height, /*BufferedImage.TYPE_INT_RGB);*/ BufferedImage.TYPE_BYTE_GRAY);

      WritableRaster imgraster  = img.getRaster();
      WritableRaster flipraster = flip.getRaster();

      for (int i=0; i<width; i++)
          for (int j=0; j<height; j++) {
              int value = imgraster.getSample(i,j,0);
              flipraster.setSample(i,j,0, 255-value );
          }
   }
   
   public static void main(String[] args) {
       String file  = args[0];
       try {
           BufferedImage img  = ImageIO.read(new File(file));
           Flipper flipper = new Flipper(img);
           ImageIO.write(flipper.getFlippedImage(), "PNG", new File("flip_"+file+".png"));
       } catch (IOException e) {
           System.out.println("Failed processing!\n"+e.toString());
       }

   }
}


