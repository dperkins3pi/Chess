package ui;

import exception.ResponseException;
import server.ServerFacade;

import java.util.Arrays;

public class GamePlayClient {
    private final ServerFacade server;
    private final String authToken;
    public GamePlayClient(String serverUrl, String authToken) {
        this.server = new ServerFacade(serverUrl);
        this.authToken = authToken;
    }

    public String eval(String input) throws ResponseException {  // Run the function based on input
        var tokens = input.toLowerCase().split(" "); // Tokenize the input
        if (tokens.length == 0) { // If no input was given, try again
            System.out.print(EscapeSequences.SET_TEXT_COLOR_RED + "Invalid Input. " +
                    EscapeSequences.SET_TEXT_COLOR_WHITE + "Please enter one of the following commands:\n");
            return "gamePlay";
        }
        String cmd = tokens[0];
        var params = Arrays.copyOfRange(tokens, 1, tokens.length);  // Get the other parameters
        switch (cmd) {
            case "logout" -> {
                logOut();
                return "loggedOut";  // Logged out
            }
            case "quit" -> {
            }
            case "help" -> help();
            default -> {
                System.out.println(EscapeSequences.SET_TEXT_COLOR_RED + "Invalid Input. " +
                        EscapeSequences.SET_TEXT_COLOR_WHITE + "Please enter one of the following commands:");
                help();
            }
        }
        return "gamePlay";
    }

    public void logOut() throws ResponseException {
        server.logOut(authToken);
        System.out.println("You successfully logged out");
    }

    public void help() {
        // Create the string to help the user
        String helpString = EscapeSequences.SET_TEXT_COLOR_YELLOW;
        helpString += "logout" + EscapeSequences.SET_TEXT_COLOR_WHITE;
        helpString += " - when you are done\n";

        helpString += EscapeSequences.SET_TEXT_COLOR_YELLOW;
        helpString += "quit" + EscapeSequences.SET_TEXT_COLOR_WHITE;
        helpString += " - playing chess\n";

        helpString += EscapeSequences.SET_TEXT_COLOR_YELLOW;
        helpString += "help" + EscapeSequences.SET_TEXT_COLOR_WHITE;
        helpString += " - with possible commands";

        System.out.println(helpString);
    }
}
