package ascii_art;

import image.SubImage;
import image_char_matching.SubImgCharMatcher;

import java.util.ArrayList;

/**
 * A public class of the package ascii_art.
 * Converts full sub-image lists to ASCII characters based on brightness matching.
 * @author Eilam Soroka and Maayan Felig
 */
public class AsciiArtAlgorithm {
    private final SubImgCharMatcher matcher;
    private final ArrayList<SubImage> subImages;
    private final int numRows;
    private final int numCols;

    /**
     * Constructs an AsciiArtAlgorithm with the given matcher, sub-images, and resolution.
     * @param matcher the SubImgCharMatcher for brightness to character mapping
     * @param subImages the list of SubImage objects to be converted
     * @param resolution the number of columns in the final ASCII art
     */
    public AsciiArtAlgorithm(SubImgCharMatcher matcher, ArrayList<SubImage> subImages, int resolution) {
        this.matcher = matcher;
        this.subImages = subImages;
        this.numRows = subImages.size() / resolution;
        this.numCols = resolution;
    }

    /**
     * Converts the list of sub-images to a 2D array of ASCII characters.
     * @return a 2D char array representing the ASCII art
     */
    public char [][] run(){
        char[][] result = new char[numRows][numCols];
        int i = 0;
        for(int row = 0; row < numRows; row++){
            for(int col = 0; col < numCols; col++){
                SubImage cur = subImages.get(i);
                result[row][col] = matcher.getCharByImageBrightness(cur.getBrightness());
                i++;
            }
        }
        return result;
    }
}
