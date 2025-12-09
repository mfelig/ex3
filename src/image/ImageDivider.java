package image;

import java.awt.*;
import java.util.ArrayList;

/**
 * A public class of the package image.
 * Divides a padded image into smaller sub-images of equal size.
 * @author Eilam Soroka and Maayan Felig
 */
public class ImageDivider {
    private final Image paddedImage;
    private final int resolution;

    /**
     * Constructs an ImageDivider with the given padded image and block size.
     * @param paddedImage the padded Image to be divided
     * @param resolution the number of blocks in a row
     */
    public ImageDivider(Image paddedImage, int resolution) {
        this.paddedImage = paddedImage;
        this.resolution = resolution;
    }

    /**
     * Divides the padded image into smaller sub-images of equal size.
     * @return an ArrayList of SubImage objects
     */
    public ArrayList<SubImage> divide() {
        int imgWidth = paddedImage.getWidth();
        int imgHeight = paddedImage.getHeight();

        int blockSize = imgWidth / resolution;
        int numBlockRows = imgHeight / blockSize;

        ArrayList<SubImage> subImages = new ArrayList<>();

        for (int i = 0; i < numBlockRows; i++) {
            for (int j = 0; j < resolution; j++) {
                Color[][] currentBlockPixels = new Color[blockSize][blockSize];

                for(int y = 0; y < blockSize; y++) {
                    for(int x = 0; x < blockSize; x++) {
                        currentBlockPixels[y][x] = paddedImage.getPixel((j * blockSize) + x,
                                (i * blockSize) + y);
                    }
                }
                subImages.add(new SubImage(currentBlockPixels));
            }
        }

        return subImages;
    }
}
