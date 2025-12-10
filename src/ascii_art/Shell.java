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
            if (command.equals("exit")) {
                break;
            }
            handleCommand(command);
        }
    }

    private String[] isCommandLegal(String line) {
        // todo take strings after the command and check if the command is legal
        String[] commandInput = line.split(" ");
        if (legalCommands.contains(commandInput[0])){
            switch (commandInput[0]) {
                case "add":
                    return handleAddInput(commandInput);
                    break;
                case "remove":
                    // todo handle remove command
                    break;
                case "res":
                    // todo handle res command
                    break;
                case "output":
                    // todo handle output command
                    break;
                default:
                    return commandInput;
            }
        }
        else {
            System.out.println("Error: illegal command");
            return null;
        }
    }

    private void handleAddInput(String[] cmd) {
        // cmd[1] should exist unless it's invalid
        if (cmd.length < 2) {
            System.out.println("Did not add due to incorrect format.");
            return;
        }

        String arg = cmd[1];

        if (arg.equals("all")) {
            // add 32..126
            return;
        }

        if (arg.equals("space")) {
            // add ' '
            return;
        }

        if (arg.length() == 1) {
            // add single char
            return;
        }

        if (arg.matches(".-.") && arg.length() == 3) {
            char start = arg.charAt(0);
            char end   = arg.charAt(2);
            // add range
            return;
        }

        System.out.println("Did not add due to incorrect format.");
    }

    private void handleCommand(String[] command) {
        switch (command[0]) {
            case "chars":
                // todo handle chars command
                break;
            case "add":
                // todo handle add command
                break;
            case "remove":
                // todo handle remove command
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
