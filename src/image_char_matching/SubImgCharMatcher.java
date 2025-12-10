package image_char_matching;
import java.util.HashMap;
import java.util.TreeSet;
import java.lang.Math;

/**
 * A public class of the package image_char_matching.
 * Matches sub-image brightness to characters from a given character set.
 * @author Eilam Soroka and Maayan Felig
 */
public class SubImgCharMatcher {
    private static final int CHAR_PIXEL_RESOLUTION = 16;

    private double minBrightness = Double.MAX_VALUE;
    private  double maxBrightness = Double.MIN_VALUE;
    private final TreeSet<Character> charSet = new TreeSet<>();
    private final HashMap<Character, Double> charNotNormalizedBrightnessMap = new HashMap<>();
    private final HashMap<Character, Double> charNormalizedBrightnessMap = new HashMap<>();

    public SubImgCharMatcher(char[] charset) {
        for(char c : charset) {
            charSet.add(c);
            boolean[][] charBooleanArray = CharConverter.convertToBoolArray(c);
            double charBrightness = calculateCharBrightness(charBooleanArray);
            charNotNormalizedBrightnessMap.put(c, charBrightness);

            // Set min and max brightness
            if(minBrightness > charBrightness) {
                minBrightness = charBrightness;
            }
            if(maxBrightness < charBrightness) {
                maxBrightness = charBrightness;
            }
        }
        createNormalizedBrightnessMap();
    }

    private double calculateCharBrightness(boolean[][] charBooleanArray) {
        //Count number of true pixels (black pixels) and divide by total number of pixels
        int numTrue = 0;
        for (boolean[] booleans : charBooleanArray) {
            for (boolean aBoolean : booleans) {
                if (aBoolean) {
                    numTrue++;
                }
            }
        }
        return (double) numTrue / (CHAR_PIXEL_RESOLUTION * CHAR_PIXEL_RESOLUTION);
    }

    private double normalizeCharBrightness(double brightness) {
        return (brightness - minBrightness) / (maxBrightness - minBrightness);
    }

    private void createNormalizedBrightnessMap() {
        charNotNormalizedBrightnessMap.forEach((c, charBrightness) ->
                charNormalizedBrightnessMap.put(c, normalizeCharBrightness(charBrightness)));
    }

    public char getCharByImageBrightness(double brightness) {
        double matchedDiff = Double.MAX_VALUE;
        char matchedChar = ' ';
        for(Character c : charSet) {
            double diff = Math.abs(brightness - charNormalizedBrightnessMap.get(c));
            if (diff < matchedDiff) {
                matchedDiff = diff;
                matchedChar = c;
            }
        }
        return matchedChar;
    }

    public void addChar(char c) {
        charSet.add(c);
        double brightness = calculateCharBrightness(CharConverter.convertToBoolArray(c));
        charNotNormalizedBrightnessMap.put(c, brightness);

        //check if we need to update min or max brightness
        if(brightness > minBrightness && brightness < maxBrightness) {
            charNormalizedBrightnessMap.put(c, normalizeCharBrightness(brightness));
            return;
        }
        if (minBrightness > brightness) {
            minBrightness = brightness;
        }
        if (maxBrightness < brightness) {
            maxBrightness = brightness;
        }
        createNormalizedBrightnessMap();
    }

    public void removeChar(char c) {
        charSet.remove(c);
        double brightness = charNotNormalizedBrightnessMap.get(c);
        charNotNormalizedBrightnessMap.remove(c);
        charNormalizedBrightnessMap.remove(c);
        if(brightness > minBrightness && brightness < maxBrightness) {
            return;
        }
        if(brightness <= minBrightness) {
            //recalculate min brightness
            minBrightness = Double.MAX_VALUE;
            for (double b : charNotNormalizedBrightnessMap.values()) {
                if (b < minBrightness) {
                    minBrightness = b;
                }
            }
        }
        if(brightness >= maxBrightness) {
            //recalculate max brightness
            maxBrightness = Double.MIN_VALUE;
            for (double b : charNotNormalizedBrightnessMap.values()) {
                if (b > maxBrightness) {
                    maxBrightness = b;
                }
            }
        }
        createNormalizedBrightnessMap();
    }
}
