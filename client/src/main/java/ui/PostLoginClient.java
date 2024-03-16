package ui;

import exception.ResponseException;
import request.JoinGameAuthRequest;
import response.GameResponseClass;
import response.ResponseClass;
import server.ServerFacade;
import java.util.Arrays;

public class PostLoginClient {
    private final ServerFacade server;
    private final String authToken;
    public PostLoginClient(String serverUrl, String authToken) {
        this.server = new ServerFacade(serverUrl);
        this.authToken = authToken;
    }

    public String eval(String input) throws ResponseException {  // Run the function based on input
        var tokens = input.toLowerCase().split(" "); // Tokenize the input
        if (tokens.length == 0) { // If no input was given, try again
            System.out.print(EscapeSequences.SET_TEXT_COLOR_RED + "Invalid Input. " +
                    EscapeSequences.SET_TEXT_COLOR_WHITE + "Please enter one of the following commands:\n");
            help();  // If the input is empty, default to help
            return "loggedIn";
        }
        String cmd = tokens[0];
        var params = Arrays.copyOfRange(tokens, 1, tokens.length);  // Get the other parameters
        switch (cmd) {
            case "create" -> {create(params);}
            case "list" -> {list();}
            case "join" -> {
                join(params);
                return "gamePlay";
            }
            case "observe" -> {
                join(params);
                return "gamePlay";
            }  // Same as join (but with no color specified)
            case "logout" -> {
                logOut();
                return "loggedOut";  // Logged out
            }
            case "quit" -> {}
            case "help" -> help();
            default -> {
                System.out.println(EscapeSequences.SET_TEXT_COLOR_RED + "Invalid Input. " +
                        EscapeSequences.SET_TEXT_COLOR_WHITE + "Please enter one of the following commands:");
                help();
            }
        }
        return "loggedIn";
    }

    public void create(String... params) throws ResponseException {
        if(params.length < 1) {  // Throw an error if an invalid number of parameters are given
            String error_string = EscapeSequences.SET_TEXT_COLOR_RED + "Incorrect number of inputs given.\n" +
                    EscapeSequences.SET_TEXT_COLOR_WHITE + "When creating a game, enter the game name";
            throw new ResponseException(error_string);
        }
        String gameName = params[0];
        ResponseClass response = server.createGame(authToken, gameName);
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

    public void join(String... params) throws ResponseException {
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
        if(params.length == 2) color = params[1];  // If they specified a color, get it

        GameResponseClass response = server.listGames(authToken);
        id = response.getID(id);   // Convert to correct id number
        server.joinGame(authToken, color, id);
        System.out.println("You joined the game");
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
