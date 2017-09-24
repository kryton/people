package utl.images;

import jjil.algorithm.RgbAvgGray;
import jjil.core.*;
import jjil.j2se.RgbImageJ2se;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Created by iholsman on 18/08/2014.
 */
public class FaceDetect {
    public static BufferedImage findFaces(BufferedImage bi, int minScale, int maxScale) throws Exception {
        try {
            // step #2 - convert BufferedImage to JJIL Image
            RgbImage im = RgbImageJ2se.toRgbImage(bi);
            // step #3 - convert image to greyscale 8-bits
            RgbAvgGray toGray = new RgbAvgGray();
            toGray.push(im);
            // step #4 - initialise face detector with correct Haar profile
            InputStream is  = new FileInputStream("conf/HCSB.txt");

            Gray8DetectHaarMultiScale detectHaar = new Gray8DetectHaarMultiScale(is, minScale, maxScale);
            // step #5 - apply face detector to grayscale image
           // detectHaar.push(toGray.getFront());
            List<Rect> results = detectHaar.pushAndReturn(toGray.getFront());
            BufferedImage cropped;
            if ( results != null && results.size()>0) {
                Rect largest = results.get(0);
                for (Rect r : results) {
                    if ( r.getArea() > largest.getArea()) {
                        largest =r;
                    }
                }
                int width = largest.getWidth();
                int height = largest.getHeight();
                int lft = largest.getLeft();
                int top = largest.getTop();
          //      ImageIO.write(bi.getSubimage(largest.getLeft(),largest.getTop(),largest.getWidth(),largest.getHeight()),"jpg",new File("/tmp/sized_1.jpg"));

                if ( lft - width/2 >0 && top - height/2 >0 && width*2 < bi.getWidth() && height*2 < bi.getHeight()) {
                    lft -= width / 2;
                    top -= height / 2;
                    width *= 2;
                    height *= 2;
                }

                cropped = bi.getSubimage(lft,top,width,height);

           //     ImageIO.write(cropped,"jpg",new File("/tmp/sized_3.jpg"));

            } else {
                cropped = bi;
            }
            return cropped;
        } catch (jjil.core.Error e) {
            e.printStackTrace();
            throw new Exception(e);
        } catch (IOException e2) {
            e2.printStackTrace();
            throw new Exception(e2);
        }
    }
    public static void main(String args[]) throws Exception {
        BufferedImage inImage  = ImageIO.read(new File(  "/Users/iholsman/Pictures/Non_Customer_Service/Terri Blessing.JPG" ));
        File f = new File("/tmp/ih.jpg");
        BufferedImage cropped=  findFaces(inImage,1,40);

        ImageIO.write(cropped,"jpg",f);

    }
}
