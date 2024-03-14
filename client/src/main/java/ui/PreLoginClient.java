package ui;

import java.util.Arrays;
import exception.ResponseException;

public class PreLoginClient {
    private final ServerFacade server;
    public PreLoginClient(String serverUrl) {
        server = new ServerFacade(serverUrl);
    }

    public void eval(String input) {  // Run the function based on input
        try {
            var tokens = input.toLowerCase().split(" "); // Tokenize the input
            if (tokens.length == 0) { // If no input was given, try again
                System.out.print(EscapeSequences.SET_TEXT_COLOR_RED + "Invalid Input. " +
                        EscapeSequences.SET_TEXT_COLOR_WHITE + "Please enter one of the following commands:\n");
                help();  // If the input is empty, default to help
                return;
            }
            String cmd = tokens[0];
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);  // Get the other parameters
            switch (cmd) {
                case "help" -> help();
                case "login" -> login(params);
                case "register" -> register(params);
                case "quit" -> {}
                default -> {
                    System.out.print(EscapeSequences.SET_TEXT_COLOR_RED + "Invalid Input. " +
                            EscapeSequences.SET_TEXT_COLOR_WHITE + "Please enter one of the following commands:\n");
                    help();
                }
            }
        } catch(ResponseException ex){
            System.out.println(ex.getMessage());
            help();
        }
    }

    public void login(String... params) throws ResponseException {
        if(params.length < 2) {  // Throw an error if an invalid number of parameters are given
            String error_string = EscapeSequences.SET_TEXT_COLOR_RED + "Invalid Input\n" + EscapeSequences.SET_TEXT_COLOR_WHITE;
            error_string += "Expected: login <USERNAME> <PASSWORD>\n";
            throw new ResponseException(error_string);
        }
        String username = params[0];
        String password = params[1];
        server.login(username, password);
    }
    public void register(String... params) throws ResponseException {
        if(params.length < 2) {  // Throw an error if an invalid number of parameters are given
            String error_string = EscapeSequences.SET_TEXT_COLOR_RED + "Invalid Input\n" + EscapeSequences.SET_TEXT_COLOR_WHITE;
            error_string += "Expected: register <USERNAME> <PASSWORD> <EMAIL>\n";
            throw new ResponseException(error_string);
        }
        String username = params[0];
        String password = params[1];
        String email = params[2];
        server.register(username, password, email);
    }

    public void help() {
        // Create the string to help the user
        String helpString = EscapeSequences.SET_TEXT_COLOR_YELLOW;
        helpString += "register <USERNAME> <PASSWORD> <EMAIL>" + EscapeSequences.SET_TEXT_COLOR_WHITE;
        helpString += " - to create an account\n";

        helpString += EscapeSequences.SET_TEXT_COLOR_YELLOW;
        helpString += "login <USERNAME> <PASSWORD>" + EscapeSequences.SET_TEXT_COLOR_WHITE;
        helpString += " - to play chess\n";

        helpString += EscapeSequences.SET_TEXT_COLOR_YELLOW;
        helpString += "quit" + EscapeSequences.SET_TEXT_COLOR_WHITE;
        helpString += " - playing chess\n";

        helpString += EscapeSequences.SET_TEXT_COLOR_YELLOW;
        helpString += "help" + EscapeSequences.SET_TEXT_COLOR_WHITE;
        helpString += " - with possible commands";

        System.out.println(helpString);
    }
}
