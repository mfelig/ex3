import ascii_art.AsciiArtAlgorithm;
import image.Image;
import image.ImagePadder;
import image.ImageDivider;
import image_char_matching.SubImgCharMatcher;

import java.io.IOException;

public class SanityTests {
    public static void main(String[] args) {
//            Color[][] pixels = {
//                    { new Color(0,0,0),     new Color(255,255,255) },
//                    { new Color(255,0,0),   new Color(0,0,255)     }
//            };

        try{
            Image img = new Image("src/examples/board.jpeg");
            // --- RUN TESTS ---
            char[] charSet = {'m', 'o'};
            Image paddedImage = testPadder(img);
//            testDivider(paddedImage);
            SubImgCharMatcher subImgCharMatcher = new SubImgCharMatcher(charSet);
            AsciiArtAlgorithm asciiArtAlgorithm = new AsciiArtAlgorithm(subImgCharMatcher,
                    new ImageDivider(paddedImage,2).divide(),2);
            char[][] asciiArt = asciiArtAlgorithm.run();
            for (char[] row : asciiArt) {
                for (char c : row) {
                    System.out.print(c + " ");
                }
                System.out.println();
            }
        } catch (IOException e){
            System.out.println("File not found");
        }


    }
//    public class ImageSanityTest {

    private static Image testPadder(Image img) {
        System.out.println("Original width: " + img.getWidth());
        System.out.println("Original height: " + img.getHeight());
        Image paddedImage = new ImagePadder(img).getPaddedImage();

        System.out.println("Padded width: " + paddedImage.getWidth());
        System.out.println("Padded height: " + paddedImage.getHeight());
        return paddedImage;
    }
    private static void testDivider(Image paddedImg) {
        ImageDivider divider = new ImageDivider(paddedImg, 2);
        var subs = divider.divide();

        System.out.println("Number of subimages = " + subs.size());  // expect 4

        for (int i = 0; i < subs.size(); i++) {
            System.out.println("Subimage " + i + " brightness = " + subs.get(i).getBrightness());
        }

        System.out.println("---------------------------------------\n");
    }


//    }
}
