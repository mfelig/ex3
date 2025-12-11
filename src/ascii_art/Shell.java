package ascii_art;

import ascii_output.AsciiOutput;
import ascii_output.ConsoleAsciiOutput;
import ascii_output.HtmlAsciiOutput;
import exceptions.IncorrectFormatException;
import exceptions.BoundariesException;
import exceptions.InputException;
import exceptions.IncorrectCharSetException;
import image.Image;
import image.ImageDivider;
import image.ImagePadder;
import image.SubImage;
import image_char_matching.SubImgCharMatcher;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.TreeSet;

import static java.lang.Math.max;

public class Shell {
    //todo add all strings to constants
    private static final int DEFAULT_RESOLUTION = 128;
    private static final char FIRST_POSSIBLE_CHAR = 32;
    private static final char LAST_POSSIBLE_CHAR = 126;
    private static final String INCORRECT_COMMAND_MESSAGE = "Did not execute due to incorrect command.";
    private static final String INCORRECT_FORMAT_MESSAGE = "Did not <placeHolder> due to incorrect format.";
    private static final String PLACEHOLDER = "<placeHolder>";
    private static final String OUT_OF_BOUNDS_RES_MESSAGE =
            "Did not change resolution due to exceeding boundaries.";
    private static final String INSUFFICIENT_CHARS_MESSAGE = "Did not execute. Charset is too small.";
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
    public void run(String filePath){
        String line = "";

        try {
            img = new Image(filePath);
        } catch (IOException e) { 
            System.out.println(e.getMessage());
            return;
        }
        imgWidth = img.getWidth();
        imgHeight = img.getHeight();

        while(!line.equals("exit")) {

            System.out.print(">>> ");
            line = KeyboardInput.readLine();
            try{
                line = handleCommandExecution(line);
                }
            catch (IncorrectFormatException e){ //todo check if this error should be caught
                System.out.println(e.getMessage());
            }
//            System.out.println(line); //
//            if (line.equals("exit")) {
//                System.out.println(line);
//            }
        }
    }

    private String handleCommandExecution(String line) throws IncorrectFormatException {
        String[] commandInput = line.split(" ");

        if (legalCommands.contains(commandInput[0])){
            try {
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
                    default:
                        throw new IncorrectFormatException(INCORRECT_COMMAND_MESSAGE);
                }
            }
            catch (InputException e) {
                System.out.println(e.getMessage());
                return "";
            }
        }
        return "next command"; //todo check why method needs return value
    }

    private void handleReverseInput() {
        reverse = !reverse; // todo understand if it has to change just for true or back and forth
    }

    private void handleAsciiArtInput() throws IncorrectCharSetException{
        if (matcher.getCharSet().size() < 2) {
            throw new IncorrectCharSetException(INSUFFICIENT_CHARS_MESSAGE);
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

    private void handleResInput(String[] commandInput) throws InputException {
        if (commandInput.length < 2) {
            System.out.println("Resolution set to " + resolutionChosen);
            return;
        }
        String update = commandInput[1];
        if (update.equals("up")) {
            if (resolutionChosen * 2 <= imgWidth) {
                resolutionChosen *= 2;
            } else {
                throw new BoundariesException(OUT_OF_BOUNDS_RES_MESSAGE);
            }
        } else if (update.equals("down")) {

            int minCharsInRow = max(1, imgWidth / imgHeight);
            if (resolutionChosen / 2 >= minCharsInRow) {
                resolutionChosen /= 2;
            } else {
                throw new BoundariesException(OUT_OF_BOUNDS_RES_MESSAGE);
            }
        } else {
            throw new IncorrectFormatException(INCORRECT_FORMAT_MESSAGE.
                    replace(PLACEHOLDER, "change resolution"));
        }
    }

    private void handleOutputInput(String[] commandInput) throws IncorrectFormatException {
        if (commandInput.length < 2 ||
                (!commandInput[1].equals("console") && !commandInput[1].equals("html"))) {
            throw new IncorrectFormatException(INCORRECT_FORMAT_MESSAGE.
                    replace(PLACEHOLDER, "change output method"));
        }
        outputFormat = commandInput[1];
        }
        // todo do i need to print in here to consul or should i just send it to the ascii art generator

    private void handleCharsInput() {
        TreeSet<Character> chars = matcher.getCharSet();
        for (char c : chars) {
            System.out.print(c + " ");
        }
        System.out.println();
    }

    private void handleAddOrRemoveInput(String[] commandsInput) throws IncorrectFormatException{
        boolean isAdd = commandsInput[0].equals("add");

        if (commandsInput.length >= 2) {
            String arg = commandsInput[1];

            if (arg.equals("all")) {
                for (char c = FIRST_POSSIBLE_CHAR; c <= LAST_POSSIBLE_CHAR; c++) {
                    if (isAdd) {
                        matcher.addChar(c);
                    } else {
                        matcher.removeChar(c);
                    }
                }
                return;
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
                return;
            }
        }
        if(isAdd){
            throw new IncorrectFormatException(INCORRECT_FORMAT_MESSAGE.replace(PLACEHOLDER, "add"));
        }
        else{
            throw new IncorrectFormatException(INCORRECT_FORMAT_MESSAGE.replace(PLACEHOLDER, "remove"));
        }
    }

    void main() {
        try {
            run("C:\\Users\\Eilam Soroka\\Desktop\\Hebrew\\year_3\\OOP\\ex3\\src\\examples\\maayan 2.jpeg");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}