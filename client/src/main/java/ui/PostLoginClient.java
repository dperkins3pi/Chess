package ui;

import exception.ResponseException;
import request.JoinGameOutput;
import response.GameResponseClass;
import server.ServerFacade;
import java.util.Arrays;

public class PostLoginClient {
    private final ServerFacade server;
    private final String authToken;
    public PostLoginClient(String serverUrl, String authToken) {
        this.server = new ServerFacade(serverUrl);
        this.authToken = authToken;
    }

    public JoinGameOutput eval(String input) throws ResponseException {  // Run the function based on input
        var tokens = input.toLowerCase().split(" "); // Tokenize the input
        if (tokens.length == 0) { // If no input was given, try again
            System.out.print(EscapeSequences.SET_TEXT_COLOR_RED + "Invalid Input. " +
                    EscapeSequences.SET_TEXT_COLOR_WHITE + "Please enter one of the following commands:\n");
            help();  // If the input is empty, default to help
            return new JoinGameOutput("loggedIn", null, null);
        }
        String cmd = tokens[0];
        var params = Arrays.copyOfRange(tokens, 1, tokens.length);  // Get the other parameters
        switch (cmd) {
            case "create" -> {create(params);}
            case "list" -> {list();}
            case "join" -> {
                return join(params);
            }
            case "observe" -> {
                return join(params);
            }  // Same as join (but with no color specified)
            case "logout" -> {
                logOut();
                return new JoinGameOutput("loggedOut", null, null);  // Logged out
            }
            case "quit" -> {}
            case "help" -> help();
            default -> {
                System.out.println(EscapeSequences.SET_TEXT_COLOR_RED + "Invalid Input. " +
                        EscapeSequences.SET_TEXT_COLOR_WHITE + "Please enter one of the following commands:");
                help();
            }
        }
        return new JoinGameOutput("loggedIn", null, null);
    }

    public void create(String... params) throws ResponseException {
        if(params.length < 1) {  // Throw an error if an invalid number of parameters are given
            String error_string = EscapeSequences.SET_TEXT_COLOR_RED + "Incorrect number of inputs given.\n" +
                    EscapeSequences.SET_TEXT_COLOR_WHITE + "When creating a game, enter the game name";
            throw new ResponseException(error_string);
        }
        String gameName = params[0];
        server.createGame(authToken, gameName);
        System.out.println("Game created. Type in 'list' to see it");
    }

    public void list() throws ResponseException {
        GameResponseClass response = server.listGames(authToken);
        System.out.println(EscapeSequences.SET_TEXT_COLOR_YELLOW + "Here are the current games:" + EscapeSequences.SET_TEXT_COLOR_WHITE);
        response.printGames();
    }

    public void logOut() throws ResponseException {
        server.logOut(authToken);
        System.out.println("You successfully logged out");
    }

    public JoinGameOutput join(String... params) throws ResponseException {
        if(params.length < 1) {  // Throw an error if an invalid number of parameters are given
            String error_string = EscapeSequences.SET_TEXT_COLOR_RED + "Incorrect number of inputs given.\n" +
                    EscapeSequences.SET_TEXT_COLOR_WHITE + "When joining a game, enter the game id as a number.\n" +
                    "You also may enter the color (if you aren't spectating)";
            throw new ResponseException(error_string);
        }
        String gameID = params[0];
        int id;
        try{
            id = Integer.parseInt(gameID);   // convert it to an integer
        } catch (Exception e){
            String error_string = EscapeSequences.SET_TEXT_COLOR_RED + "Incorrect number of inputs given.\n" +
                    EscapeSequences.SET_TEXT_COLOR_WHITE + "When joining a game, enter the game id as a number.\n" +
                    "You also may enter the color (if you aren't spectating)";
            throw new ResponseException(error_string);
        }
        String color = null;
        String output = "observe";
        if(params.length == 2) {
            color = params[1];  // If they specified a color, get it
            output = "join";
        }

        GameResponseClass response = server.listGames(authToken);
        id = response.getID(id);   // Convert to correct id number
        server.joinGame(authToken, color, id);
        System.out.println("You joined the game");
        return new JoinGameOutput(output, id, color);
    }

    public void help() {
        // Create the string to help the user
        String helpString = EscapeSequences.SET_TEXT_COLOR_YELLOW;
        helpString += "create <NAME>" + EscapeSequences.SET_TEXT_COLOR_WHITE;
        helpString += " - a game\n";

        helpString += EscapeSequences.SET_TEXT_COLOR_YELLOW;
        helpString += "list" + EscapeSequences.SET_TEXT_COLOR_WHITE;
        helpString += " - games\n";

        helpString += EscapeSequences.SET_TEXT_COLOR_YELLOW;
        helpString += "join <ID> [WHITE|BLACK|<empty>]" + EscapeSequences.SET_TEXT_COLOR_WHITE;
        helpString += " - a game\n";

        helpString += EscapeSequences.SET_TEXT_COLOR_YELLOW;
        helpString += "observe <ID>" + EscapeSequences.SET_TEXT_COLOR_WHITE;
        helpString += " - a game\n";

        helpString += EscapeSequences.SET_TEXT_COLOR_YELLOW;
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
