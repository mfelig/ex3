package ascii_art;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.HashSet;
import java.util.SplittableRandom;

public class Shell {
        private final HashSet<String>  legalCommands = new HashSet<>(Arrays.asList(
            "chars", "add", "remove", "res", "output", "reverse", "asciiArt", "exit"
        ));

    public void run(String imageName){
        String line = "";

        while(true) {
            System.out.print(">>> ");
            line = KeyboardInput.readLine();
            String[] command = isCommandLegal(line);
            if (command[0].equals("exit")) {
                break;
            }
        }
    }

    private String[] isCommandLegal(String line) {
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
            return null;
        }
        return null; //todo change all the return nulls
    }

    private void handleAddOrRemoveInput(String[] commandsInput) {
        if (commandsInput.length < 2) {
            System.out.println("Did not add due to incorrect format.");
            return;
        }

        String arg = commandsInput[1];

        if (arg.equals("all")) {
            // add 32..126
            if (commandsInput[0].equals("add")) {
                // add all
            } else {
                // remove all
            }
            return;
        }

        if (arg.equals("space")) {
            // add ' '
            if (commandsInput[0].equals("add")) {
                // add space
            }
            else {
            }
            // remove space
            return;
        }

        if (arg.length() == 1) {
            if (commandsInput[0].equals("add")) {
                // add char
            }
            else {
                // remove char
            }
            return;
        }

        if (arg.matches(".-.") && arg.length() == 3) {
            char start = arg.charAt(0);
            char end   = arg.charAt(2);
            if (start > end) {
                char temp = start;
                start = end;
                end = temp;
            }
            if (commandsInput[0].equals("add")) {
                // add Range
            }
            else {
                // remove Range
            }
            return;
        }

        System.out.println("Did not add due to incorrect format.");
    }

    public Shell() {
        // todo understand what is  needed here
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
