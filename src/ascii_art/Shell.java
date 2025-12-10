package ascii_art;

import image_char_matching.SubImgCharMatcher;

import java.util.Arrays;
import java.util.HashSet;

public class Shell {
    private final HashSet<String>  legalCommands = new HashSet<>(Arrays.asList(
            "chars", "add", "remove", "res", "output", "reverse", "asciiArt", "exit"
    ));
    private SubImgCharMatcher matcher;
    public Shell() {
        matcher = new SubImgCharMatcher("0123456789".toCharArray());
    }
    public void run(String imageName){
        String line = "";
        // demonstration

        while(true) {
            System.out.print(">>> ");
            line = KeyboardInput.readLine();
            String[] command = handleCommandExacution(line, imageName);
            if (command[0].equals("exit")) {
                break;
            }
        }
    }

    private String[] handleCommandExacution(String line, String imageName) {
        // todo take strings after the command and check if the command is legal
        String[] commandInput = line.split(" ");

        if (legalCommands.contains(commandInput[0])){
            switch (commandInput[0]) {
                case "exit":
                    return new String[]{"exit"};
                case "chars":
                    // todo handle chars command
                    break;
                case "add":
                    handleAddOrRemoveInput(commandInput);
                    break;
                case "remove":
                    handleAddOrRemoveInput(commandInput);
                    break;
                case "res":
                    // todo handle res command
                    break;
                case "output":
                    // todo handle output command
                    break;
                case "reverse":
                    // todo handle asciiArt command
                    break;
                case "asciiArt":
                    // todo handle asciiArt command
                    break;

            }
        }
        else {
            System.out.println("Error: illegal command");
            return null; // todo exception
        }
        return null; //todo change all the return nulls
    }

    private void handleAddOrRemoveInput(String[] commandsInput) {
        boolean isAdd = commandsInput[0].equals("add");
        if (commandsInput.length < 2) {
            System.out.println("Did not add due to incorrect format.");
            return;
        }

        String arg = commandsInput[1];

        if (arg.equals("all")) {
            // add 32..126
            for (char c = 32; c <= 126; c++) {
                if (isAdd) {
                    matcher.addChar(c);
                } else {
                    matcher.removeChar(c);
                }
                return;
            }

            if (arg.equals("space")) {
                // add ' '
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

            System.out.println("Did not add due to incorrect format.");
        }
    }
    void main() {
        try {
            run("defaultImage.jpg");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        run("");
    }
}