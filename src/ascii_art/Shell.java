package ascii_art;

import image_char_matching.SubImgCharMatcher;

import java.util.Arrays;
import java.util.HashSet;
import java.util.TreeSet;

public class Shell {
    private static final int DEFAULT_RESOLUTION = 2;
    private static final char FIRST_POSSIBLE_CHAR = 32;
    private static final char LAST_POSSIBLE_CHAR = 126;
    private final HashSet<String>  legalCommands = new HashSet<>(Arrays.asList(
            "chars", "add", "remove", "res", "output", "reverse", "asciiArt", "exit"
    ));
    private final SubImgCharMatcher matcher;
    private final int resulutionChosen;
    private String outputFormat;

    public Shell() {
        matcher = new SubImgCharMatcher("0123456789".toCharArray());
        outputFormat = "console";
        resulutionChosen = DEFAULT_RESOLUTION;
    }
    public void run(String imageName){
        String line = "";
        // demonstration

        while(!line.equals("exit")) {

            System.out.print(">>> ");
            line = KeyboardInput.readLine();
            line = handleCommandExacution(line, imageName);
//            System.out.println(line); //todo for debug delete at the end
//            if (line.equals("exit")) {
//                System.out.println(line);
//            }
        }
    }

    private String handleCommandExacution(String line, String imageName) {
        // todo take strings after the command and check if the command is legal
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
                    // todo handle res command
                    break;
                case "output":
                    handleOutputInput(commandInput);
                    break;
                case "reverse":
                    // todo handle asciiArt command
                    break;
                case "asciiArt":
                    // todo handle asciiArt command
                    break;

            }
            return "next command";
        }
        else {
            System.out.println("Error: illegal command");
            return ""; // todo exception
        }
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