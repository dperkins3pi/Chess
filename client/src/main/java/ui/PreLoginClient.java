package ui;

import java.util.Arrays;
import exception.ResponseException;
import response.ResponseClass;
import server.ServerFacade;

public class PreLoginClient {
    private final ServerFacade server;
    public PreLoginClient(String serverUrl) {
        server = new ServerFacade(serverUrl);
    }

    public String eval(String input) throws ResponseException {  // Run the function based on input
        var tokens = input.toLowerCase().split(" "); // Tokenize the input
        if (tokens.length == 0) { // If no input was given, try again
            System.out.print(EscapeSequences.SET_TEXT_COLOR_RED + "Invalid Input. " +
                    EscapeSequences.SET_TEXT_COLOR_WHITE + "Please enter one of the following commands:\n");
            help();  // If the input is empty, default to help
            return null;
        }
        String cmd = tokens[0];
        var params = Arrays.copyOfRange(tokens, 1, tokens.length);  // Get the other parameters
        switch (cmd) {
            case "help" -> help();
            case "login" -> {return login(params);}
            case "register" -> {if(register(params)) return null;}
            case "quit" -> {}
            default -> {
                System.out.print(EscapeSequences.SET_TEXT_COLOR_RED + "Invalid Input. " +
                        EscapeSequences.SET_TEXT_COLOR_WHITE + "Please enter one of the following commands:\n");
                help();
            }
        }
        return null;
    }

    public String login(String... params) throws ResponseException {
        if(params.length < 2) {  // Throw an error if an invalid number of parameters are given
            String error_string = EscapeSequences.SET_TEXT_COLOR_RED + "Incorrect number of inputs given.\n" +
                    EscapeSequences.SET_TEXT_COLOR_WHITE + "When logging in, enter both your username and password\n";
            throw new ResponseException(error_string);
        }
        String username = params[0];
        String password = params[1];
        ResponseClass request = server.login(username, password);
        System.out.println("Logged in as " + request.getUsername());
        return request.getAuthToken();
    }
    public Boolean register(String... params) throws ResponseException {
        if(params.length < 3) {  // Throw an error if an invalid number of parameters are given
            String error_string = EscapeSequences.SET_TEXT_COLOR_RED + "Incorrect number of inputs given.\n" +
                    EscapeSequences.SET_TEXT_COLOR_WHITE + "When registering in, enter your username, password, ane email\n";
            throw new ResponseException(error_string);
        }
        String username = params[0];
        String password = params[1];
        String email = params[2];
        ResponseClass request = server.register(username, password, email);
        System.out.println("Logged in as " + request.getUsername());
        return true;
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
