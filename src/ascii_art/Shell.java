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

import java.util.TreeSet;

import static java.lang.Math.max;
/**
 * A public class of the package ascii_art.
 * Handles user input and executes commands for ASCII art generation.
 * The Cli like shell allows users to modify character sets, resolution,
 * output format, and generate ASCII art from images.
 * @author Eilam Soroka and Maayan Felig
 */
public class Shell {
    //todo add all strings to constants
    private static final int DEFAULT_RESOLUTION = 128;
    private static final char FIRST_POSSIBLE_CHAR = 32;
    private static final char LAST_POSSIBLE_CHAR = 126;
    private static final String DEFAULT_CHARSET = "0123456789";
    private static final String INCORRECT_COMMAND_MESSAGE = "Did not execute due to incorrect command.";
    private static final String INCORRECT_FORMAT_MESSAGE = "Did not <placeHolder> due to incorrect format.";
    private static final String PLACEHOLDER = "<placeHolder>";
    private static final String OUT_OF_BOUNDS_RES_MESSAGE =
            "Did not change resolution due to exceeding boundaries.";
    private static final String INSUFFICIENT_CHARS_MESSAGE = "Did not execute. Charset is too small.";
    private static final String PRINTING_FORMAT = ">>> ";
    private static final String SPACE = " ";
    private static final String EMPTY_STRING = "";
    private static final String CONSOLE_OUTPUT_FORMAT = "console";
    private static final String HTML_OUTPUT_FORMAT = "html";
    private static final String EXIT_COMMAND = "exit";
    private static final String CHARS_COMMAND = "chars";
    private static final String ADD_COMMAND = "add";
    private static final String REMOVE_COMMAND = "remove";
    private static final String RESOLUTION_COMMAND = "res";
    private static final String OUTPUT_COMMAND = "output";
    private static final String REVERSE_COMMAND = "reverse";
    private static final String ASCII_ART_COMMAND = "asciiArt";
    private static final String OUTPUT_FILE_NAME = "out.html";
    private static final String FONT_NAME = "Courier New";
    private static final String RESOLUTION_CHOSEN_MESSAGE = "Resolution set to ";
    private static final String RESOLUTION_UP_COMMAND = "up";
    private static final String RESOLUTION_DOWN_COMMAND = "down";
    private static final String CHANGE_RESOLUTION = "change resolution";
    private static final String CHANGE_OUTPUT = "change output method";
    private static final String ENTIRE_PRINTABLE_CHAR_SET = "all";
    private static final String RANGE_REGEX = ".-.";
    private static final char SPACE_CHAR = ' ';
    private static final int MINIMUM_CHARSET_SIZE_FOR_ASCII_ART = 2;
    private static final int RESOLUTION_COMMAND_LENGTH = 2;
    private static final int RESOLUTION_FACTOR = 2;
    private static final int OUTPUT_COMMAND_LENGTH = 2;
    private static final int ADD_OR_REMOVE_MINIMUM_COMMAND_LENGTH = 2;
    private static final int ADD_OR_REMOVE_RANGE_LENGTH = 3;
    private static final int RANGE_START = 0;
    private static final int RANGE_END = 2;
    private final SubImgCharMatcher matcher;
    private int resolutionChosen;
    private String outputFormat;
    private Image img;
    private int imgWidth;
    private int imgHeight;
    private boolean reverse = false;

    /**
     * Constructs a Shell with default settings.
     */
    public Shell() {
        matcher = new SubImgCharMatcher(DEFAULT_CHARSET.toCharArray());
        outputFormat = CONSOLE_OUTPUT_FORMAT;
        resolutionChosen = DEFAULT_RESOLUTION;
    }
    /**
     * Runs the shell, processing user commands for ASCII art generation.
     * @param filePath the path to the image file to be processed
     */
    public void run(String filePath){
        String line = EMPTY_STRING;

        try {
            img = new Image(filePath);
        } catch (IOException e) { 
            System.out.println(e.getMessage());
            return;
        }
        imgWidth = img.getWidth();
        imgHeight = img.getHeight();

        while(!line.equals(EXIT_COMMAND)) {

            System.out.print(PRINTING_FORMAT);
            line = KeyboardInput.readLine();
            try{
                line = handleCommandExecution(line);
                }
            catch (IncorrectFormatException e){
                System.out.println(e.getMessage());
            }
        }
    }

    private String handleCommandExecution(String line) throws IncorrectFormatException {
        String[] commandInput = line.split(SPACE);

        try {
            switch (commandInput[0]) {
                case EXIT_COMMAND:
                    return EXIT_COMMAND;
                case CHARS_COMMAND:
                    handleCharsInput();
                    return CHARS_COMMAND;
                case ADD_COMMAND:
                    handleAddOrRemoveInput(commandInput);
                    return ADD_COMMAND;
                case REMOVE_COMMAND:
                    handleAddOrRemoveInput(commandInput);
                    return REMOVE_COMMAND;
                case RESOLUTION_COMMAND:
                    handleResInput(commandInput);
                    return RESOLUTION_COMMAND;
                case OUTPUT_COMMAND:
                    handleOutputInput(commandInput);
                    return OUTPUT_COMMAND;
                case REVERSE_COMMAND:
                    handleReverseInput();
                    return REVERSE_COMMAND;
                case ASCII_ART_COMMAND:
                    handleAsciiArtInput();
                    return ASCII_ART_COMMAND;
                default:
                    throw new IncorrectFormatException(INCORRECT_COMMAND_MESSAGE);
            }
        }
        catch (InputException e) {
            System.out.println(e.getMessage());
            return SPACE;
        }
    }

    private void handleReverseInput() {
        reverse = !reverse;
    }

    private void handleAsciiArtInput() throws IncorrectCharSetException{
        if (matcher.getCharSet().size() < MINIMUM_CHARSET_SIZE_FOR_ASCII_ART) {
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
        if (outputFormat.equals(CONSOLE_OUTPUT_FORMAT)) {
            output = new ConsoleAsciiOutput();
        }
        else {
            output = new HtmlAsciiOutput(OUTPUT_FILE_NAME, FONT_NAME);
        }


        output.out(asciiMatrix);

    }

    private void handleResInput(String[] commandInput) throws InputException {
        if (commandInput.length < RESOLUTION_COMMAND_LENGTH) {
            System.out.println(RESOLUTION_CHOSEN_MESSAGE + resolutionChosen);
            return;
        }
        String update = commandInput[1];
        if (update.equals(RESOLUTION_UP_COMMAND)) {
            if (resolutionChosen * RESOLUTION_FACTOR <= imgWidth) {
                resolutionChosen *= RESOLUTION_FACTOR;
            } else {
                throw new BoundariesException(OUT_OF_BOUNDS_RES_MESSAGE);
            }
        } else if (update.equals(RESOLUTION_DOWN_COMMAND)) {

            int minCharsInRow = max(1, imgWidth / imgHeight);
            if (resolutionChosen / RESOLUTION_FACTOR >= minCharsInRow) {
                resolutionChosen /= RESOLUTION_FACTOR;
            } else {
                throw new BoundariesException(OUT_OF_BOUNDS_RES_MESSAGE);
            }
        } else {
            throw new IncorrectFormatException(INCORRECT_FORMAT_MESSAGE.
                    replace(PLACEHOLDER, CHANGE_RESOLUTION));
        }
    }

    private void handleOutputInput(String[] commandInput) throws IncorrectFormatException {
        if (commandInput.length < OUTPUT_COMMAND_LENGTH ||
                (!commandInput[1].equals(CONSOLE_OUTPUT_FORMAT) &&
                        !commandInput[1].equals(HTML_OUTPUT_FORMAT))) {
            throw new IncorrectFormatException(INCORRECT_FORMAT_MESSAGE.
                    replace(PLACEHOLDER, CHANGE_OUTPUT));
        }
        outputFormat = commandInput[1];
        }

    private void handleCharsInput() {
        TreeSet<Character> chars = matcher.getCharSet();
        for (char c : chars) {
            System.out.print(c + SPACE);
        }
        System.out.println();
    }

    private void handleAddOrRemoveInput(String[] commandsInput) throws IncorrectFormatException{
        boolean isAdd = commandsInput[0].equals(ADD_COMMAND);

        if (commandsInput.length >= ADD_OR_REMOVE_MINIMUM_COMMAND_LENGTH) {
            String arg = commandsInput[1];

            if (arg.equals(ENTIRE_PRINTABLE_CHAR_SET)) {
                for (char c = FIRST_POSSIBLE_CHAR; c <= LAST_POSSIBLE_CHAR; c++) {
                    if (isAdd) {
                        matcher.addChar(c);
                    } else {
                        matcher.removeChar(c);
                    }
                }
                return;
            }

            if (arg.equals(SPACE)) {
                if (isAdd) {
                    matcher.addChar(SPACE_CHAR);
                } else {
                    matcher.removeChar(SPACE_CHAR);
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

            if (arg.matches(RANGE_REGEX) && arg.length() == ADD_OR_REMOVE_RANGE_LENGTH) {
                char start = arg.charAt(RANGE_START);
                char end = arg.charAt(RANGE_END);
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
            throw new IncorrectFormatException(INCORRECT_FORMAT_MESSAGE.replace(PLACEHOLDER, ADD_COMMAND));
        }
        else{
            throw new IncorrectFormatException(INCORRECT_FORMAT_MESSAGE.replace(PLACEHOLDER, REMOVE_COMMAND));
        }
    }
    /**
     * The main method to run the Shell for ASCII art generation.
     * @param args command-line arguments (not used)
     *
     */
    void main(String[] args) {
        try {
            Shell shell = new Shell();
            shell.run(args[0]);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}

