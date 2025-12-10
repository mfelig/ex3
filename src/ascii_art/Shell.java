package ascii_art;

import ascii_output.AsciiOutput;
import ascii_output.ConsoleAsciiOutput;
import ascii_output.HtmlAsciiOutput;
import image.Image;
import image.ImageDivider;
import image.ImagePadder;
import image.SubImage;
import image_char_matching.SubImgCharMatcher;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.TreeSet;

import static java.lang.Math.max;

public class Shell {
    private static final int DEFAULT_RESOLUTION = 2;
    private static final char FIRST_POSSIBLE_CHAR = 32;
    private static final char LAST_POSSIBLE_CHAR = 126;
    private final HashSet<String>  legalCommands = new HashSet<>(Arrays.asList(
            "chars", "add", "remove", "res", "output", "reverse", "asciiArt", "exit"
    ));
    private final SubImgCharMatcher matcher;
    private int resolutionChosen;
    private String outputFormat;
    private Image img;
    private int imgWidth;
    private int imgHeight;
    private boolean reverse = false;

    public Shell() {
        matcher = new SubImgCharMatcher("0123456789".toCharArray());
        outputFormat = "console";
        resolutionChosen = DEFAULT_RESOLUTION;
    }
    public void run(String imageName){
        String line = "";

        try {
            img = new Image(imageName);
        } catch (Exception e) { // todo is this the right exception?
            System.out.println("Error loading image.");
            return;
        }
        imgWidth = img.getWidth();
        imgHeight = img.getHeight();

        while(!line.equals("exit")) {

            System.out.print(">>> ");
            line = KeyboardInput.readLine();
            line = handleCommandExecution(line);
//            System.out.println(line); //todo print if the command is illeagl
//            if (line.equals("exit")) {
//                System.out.println(line);
//            }
        }
    }

    private String handleCommandExecution(String line) {
        String[] commandInput = line.split(" ");

        if (legalCommands.contains(commandInput[0])){
            switch (commandInput[0]) {
                case "exit":
                    return "exit";
                case "chars":
                    handleCharsInput();
                    return "chars";
                case "add":
                    handleAddOrRemoveInput(commandInput);
                    return "add";
                case "remove":
                    handleAddOrRemoveInput(commandInput);
                    return "remove";
                case "res":
                    handleResInput(commandInput);
                    return "res";
                case "output":
                    handleOutputInput(commandInput);
                    return "output";
                case "reverse":
                    handleReverseInput();
                    return "reverse";
                case "asciiArt":
                    handleAsciiArtInput();
                    return "asciiArt";

            }
            return "next command";
        }
        else {
            System.out.println("Error: illegal command");
            return ""; // todo exception
        }
    }

    private void handleReverseInput() {
        reverse = !reverse; // todo understand if it has to change just for true or back and forth
//        System.out.println("Reverse set to " + reverse);
    }

    private void handleAsciiArtInput() {
        if (matcher.getCharSet().size() < 2) {
            System.out.println("Did not execute. Charset is too small.");
            return;
        }

        ImagePadder padder = new ImagePadder(img);
        Image padded = padder.getPaddedImage();

        ImageDivider divider = new ImageDivider(padded, resolutionChosen);
        ArrayList<SubImage> subs = divider.divide();

        AsciiArtAlgorithm algo =
                new AsciiArtAlgorithm(matcher, subs, resolutionChosen, reverse);
        char[][] asciiMatrix = algo.run();

        AsciiOutput output;
        if (outputFormat.equals("console")) {
            output = new ConsoleAsciiOutput();
        }
        else {
            output = new HtmlAsciiOutput("out.html", "Courier New");
        }


        output.out(asciiMatrix);

    }

    private void handleResInput(String[] commandInput) {
        if (commandInput.length < 2) {
            System.out.println("Resolution set to " + resolutionChosen);
            return;
        }
        String update = commandInput[1];
        if (update.equals("up")) {
            if (resolutionChosen * 2 <= imgWidth) {
                resolutionChosen *= 2;
            }
            else{
                System.out.println("Did not change resolution due to exceeding boundaries.");
            }
        }
        if (update.equals("down")) {
            int minCharsInRow = max(1, imgWidth/imgHeight);
            if (resolutionChosen / 2 >= minCharsInRow) {
                resolutionChosen /= 2;
            }
            else {
                System.out.println("Did not change resolution due to exceeding boundaries.");
            }
        }
        System.out.println("Did not change resolution due to incorrect format.");
    }

    private void handleOutputInput(String[] commandInput) {
        if (commandInput.length < 2) {
            System.out.println("Did not change output due to incorrect format.");
            return;
        }
        String format = commandInput[1];
        if (format.equals("console") || format.equals("html")) {
            outputFormat = format;
        }
        // todo do i need to print in here to consul or should i just send it to the ascii art generator
    }

    private void handleCharsInput() {
        TreeSet<Character> chars = matcher.getCharSet();
        for (char c : chars) {
            System.out.print(c + " ");
        }
        System.out.println();
    }

    private void handleAddOrRemoveInput(String[] commandsInput) {
        boolean isAdd = commandsInput[0].equals("add");
        if (commandsInput.length < 2) {
            System.out.println("Did not add due to incorrect format.");
            return;
        }

        String arg = commandsInput[1];

        if (arg.equals("all")) {
            for (char c = FIRST_POSSIBLE_CHAR; c <= LAST_POSSIBLE_CHAR; c++) {
                if (isAdd) {
                    matcher.addChar(c);
                } else {
                    matcher.removeChar(c);
                }
            }
        }

        if (arg.equals("space")) {
            if (isAdd) {
                matcher.addChar(' ');
            } else {
                matcher.removeChar(' ');
            }
            return;
        }

        if (arg.length() == 1) {
            if (isAdd) {
                matcher.addChar(arg.charAt(0));
            } else {
                matcher.removeChar(arg.charAt(0));
            }
            return;
        }

        if (arg.matches(".-.") && arg.length() == 3) {
            char start = arg.charAt(0);
            char end = arg.charAt(2);
            if (start > end) {
                char temp = start;
                start = end;
                end = temp;
            }
            for (char c = start; c <= end; c++) {
                if (isAdd) {
                    matcher.addChar(c);
                } else {
                    matcher.removeChar(c);
                }
            }
        }

    }
    void main() {
        try {
            run("defaultImage.jpg");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}