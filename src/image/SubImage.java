package image;

import java.awt.*;

public class SubImage {

    private double brightness;
    private Color[][] pixels;
    private static final int MAX_BRIGHTNESS = 255;

    public SubImage(Color[][] pixels) {
        this.brightness = calculateBrightness(pixels);
        this.pixels = pixels;
    }

    private double calculateBrightness(Color[][] pixels) {
        double total = 0.0;
        for (Color[] row : pixels) {
            for (Color pixel : row) {
                double greyPixel = pixel.getRed() * 0.2126 + pixel.getGreen() * 0.7152
                        + pixel.getBlue() * 0.0722;
                total += greyPixel;
            }
        }
        return total / (pixels.length * pixels[0].length * MAX_BRIGHTNESS);
    }

    public double getBrightness() {
        return brightness;
    }
}
