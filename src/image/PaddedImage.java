package image;

import java.awt.Color;
/**
 * A package-private class of the package image.
 * Pads an image to the nearest power of two dimensions with white pixels.
 * @author Eilam Soroka and Maayan Felig
 */
class PaddedImage {

    private final Image paddedImage;

    /**
     * Constructs a PaddedImage by padding the original image to the nearest power of two dimensions.
     *
     * @param original
     */
    public PaddedImage(Image original) {
        this.paddedImage = createPaddedImage(original);
    }
    /**
     * Returns the padded image.
     *
     * @return the padded Image
     */
    public Image getImage() {
        return paddedImage;
    }

    /**
     * Creates a new Image padded to the nearest power of two dimensions with white pixels.
     * @param original
     * @return the padded Image
     */
    private Image createPaddedImage(Image original) {
        int origWidth = original.getWidth();     // number of columns
        int origHeight = original.getHeight();   // number of rows

        int newWidth = nextPowerOfTwo(origWidth);
        int newHeight = nextPowerOfTwo(origHeight);

        Color[][] paddedPixels = new Color[newHeight][newWidth];

        Color white = new Color(255, 255, 255);

        // Fill entire padded image with white
        for (int row = 0; row < newHeight; row++) {
            for (int col = 0; col < newWidth; col++) {
                paddedPixels[row][col] = white;
            }
        }

        int offsetX = (newWidth - origWidth) / 2;
        int offsetY = (newHeight - origHeight) / 2;

        // Copy original image pixels into the center of the padded image
        for (int y = 0; y < origHeight; y++) {
            for (int x = 0; x < origWidth; x++) {
                paddedPixels[y + offsetY][x + offsetX] = original.getPixel(x, y);
            }
        }

        return new Image(paddedPixels, newWidth, newHeight);
    }
    /**
     * Computes the next power of two greater than or equal to n.
     * @param n
     * @return the next power of two
     */
    private int nextPowerOfTwo(int n) {
        int p = 1;
        while (p < n){
            p *= 2;
        }
        return p;
    }
}
