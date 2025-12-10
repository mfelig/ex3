package image;

import java.awt.*;

/**
 * A public class of the package image.
 * Represents a sub-image and calculates its brightness.
 * @author Eilam Soroka and Maayan Felig
 */
public class SubImage {

    private final double brightness;
    private final Color[][] pixels;
    private static final int MAX_BRIGHTNESS = 255;

    /**
     * Constructs a SubImage with the given pixel array and calculates its brightness.
     * @param pixels a 2D array of Color objects representing the pixels of the sub-image
     */
    public SubImage(Color[][] pixels) {
        if(pixels == null || pixels.length == 0 || pixels[0].length == 0) {
            throw new IllegalArgumentException("Pixel array cannot be null or empty");
        }
        //todo check what to do with invalid input
        this.pixels = pixels;
        this.brightness = calculateBrightness();
    }

    private double calculateBrightness() {
        double total = 0.0;
        for (Color[] row : this.pixels) {
            for (Color pixel : row) {
                double greyPixel = pixel.getRed() * 0.2126 + pixel.getGreen() * 0.7152
                        + pixel.getBlue() * 0.0722;
                total += greyPixel;
            }
        }
        return total / (pixels.length * pixels[0].length * MAX_BRIGHTNESS);
    }

    /**
     * Returns the brightness of the sub-image.
     * @return the brightness as a double value
     */
    public double getBrightness() {
        return this.brightness;
    }
}
