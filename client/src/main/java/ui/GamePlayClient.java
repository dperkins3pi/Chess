package ui;

import chess.*;
import exception.ResponseException;
import handler.GameHandler;
import request.JoinGameOutput;
import response.GameResponseClass;
import server.ServerFacade;
import server.WebSocketFacade;

import java.util.ArrayList;
import java.util.Arrays;

public class GamePlayClient {
    private final String authToken;
    private Integer gameID;
    private final ServerFacade server;
    private final WebSocketFacade wsFacade;
    private final String color;
    public GamePlayClient(String serverUrl, String authToken, GameHandler gameHandler, JoinGameOutput output) throws ResponseException {
        this.authToken = authToken;
        this.server = new ServerFacade(serverUrl);
        this.wsFacade = new WebSocketFacade(serverUrl, gameHandler);
        // Join game from websocket as well
        if ("observe".equals(output.getState())) joinObserver(output.getGameID());
        else if ("join".equals(output.getState())) join(output.getGameID(), output.getColor());
        else if ("failed".equals(output.getState())) joinObserver(null);
        this.color = output.getColor();
    }

    private ChessGame getGame() throws ResponseException {
        GameResponseClass response = server.listGames(authToken);
        return response.getGame(this.gameID);
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
            case "help" -> help();
            case "redraw" -> draw(this.getGame());  // TODO: Fix this
            case "leave" -> {return leaveGame();}
            case "move" -> {move(params);}  // TODO: Implenet this
            case "resign" -> {return "loggedIn";}
            case "highlight" -> {}   // TODO: Implenet this
            default -> {
                System.out.println(EscapeSequences.SET_TEXT_COLOR_RED + "Invalid Input. " +
                        EscapeSequences.SET_TEXT_COLOR_WHITE + "Please enter one of the following commands:");
                help();
            }
        }
        return "gamePlay";
    }
    private void join(Integer gameID, String color) throws ResponseException {
        wsFacade.joinPlayer(authToken, gameID, color);
        this.gameID = gameID;
    }
    private void joinObserver(Integer gameID) throws ResponseException {
        wsFacade.joinObserver(authToken, gameID);
        this.gameID = gameID;
    }
    private String leaveGame() throws ResponseException {
        wsFacade.leaveGame(authToken, gameID);
        return "loggedIn";
    }

    private ArrayList<Integer> readMoveInput(String... params) throws ResponseException {
        ArrayList<Integer> input = new ArrayList<Integer>();
        if(params.length < 2) {  // Throw an error if an invalid number of parameters are given
            String error_string = EscapeSequences.SET_TEXT_COLOR_RED + "Incorrect number of inputs given.\n" +
                    EscapeSequences.SET_TEXT_COLOR_WHITE + "When making a move, please enter the starting and ending position\n" +
                    "Ex: 'move b2 b3' would move the piece at position b2 to position b3";
            throw new ResponseException(error_string);
        }
        String startString = params[0];
        String endString = params[1];

        var tokens1 = startString.split(""); // Tokenize the input
        var tokens2 = endString.split(""); // Tokenize the input
        if (tokens1.length != 2 || tokens2.length != 2) { // If no input was given, try again
            String error_string = EscapeSequences.SET_TEXT_COLOR_RED + "Invalid positions given.\n" +
                    EscapeSequences.SET_TEXT_COLOR_WHITE + "The starting and ending positions should be one letter followed by one number without a space\n" +
                    "Ex: 'move b2 b3' would move the piece at position b2 to position b3";
            throw new ResponseException(error_string);
        }
        String startPositionStringX = tokens1[0];
        String startPositionStringY = tokens1[1];
        String endPositionStringX = tokens2[0];
        String endPositionStringY = tokens2[1];

        int startPositionX;
        int startPositionY;
        int endPositionX;
        int endPositionY;
        try{
            startPositionY = Integer.parseInt(startPositionStringY);   // convert it to an integer
            endPositionY = Integer.parseInt(endPositionStringY);   // convert it to an integer
        } catch (Exception e){
            String error_string = EscapeSequences.SET_TEXT_COLOR_RED + "Invalid positions given.\n" +
                    EscapeSequences.SET_TEXT_COLOR_WHITE + "The starting and ending positions should be one letter followed by one number without a space\n" +
                    "Ex: 'move b2 b3' would move the piece at position b2 to position b3";
            throw new ResponseException(error_string);
        }
        startPositionX = startPositionStringX.charAt(0) - 'a' + 1;
        endPositionX = endPositionStringX.charAt(0) - 'a' + 1;
        if((startPositionX < 1 || startPositionX > 8) || (startPositionY < 1 || startPositionY > 8) ||
                (endPositionX < 1 || endPositionX > 8) || (endPositionY < 1 || endPositionY > 8)){
            String error_string = EscapeSequences.SET_TEXT_COLOR_RED + "Invalid positions given.\n" +
                    EscapeSequences.SET_TEXT_COLOR_WHITE + "The first part starting and ending positions should be between a and h\n" +
                    "The second part starting and ending positions should be between 1 and 8\n" +
                    "Ex: 'move b2 b3' would move the piece at position b2 to position b3";
            throw new ResponseException(error_string);
        }
        input.add(startPositionX);
        input.add(startPositionY);
        input.add(endPositionX);
        input.add(endPositionY);
        return input;
    }

    public void move(String... params) throws ResponseException {
        ArrayList<Integer> input = readMoveInput(params);
        Integer startPositionX = input.get(0);
        Integer startPositionY = input.get(1);
        Integer endPositionX = input.get(2);
        Integer endPositionY = input.get(3);
        ChessPosition startPosition = new ChessPosition(startPositionY, startPositionX);
        ChessPosition endPosition = new ChessPosition(endPositionY, endPositionX);
        ChessMove move = new ChessMove(startPosition, endPosition, null);  //TODO: Worrry about promotions

        ChessGame game = getGame();
        ChessBoard board = game.getBoard();
        ChessPiece piece = board.getPiece(startPosition);
        if(piece == null || !piece.getTeamColor().toString().equalsIgnoreCase(this.color)){
            String error_string = EscapeSequences.SET_TEXT_COLOR_RED + "Invalid positions given.\n" +
                    EscapeSequences.SET_TEXT_COLOR_WHITE + "The starting position must be on a piece of your team";
            throw new ResponseException(error_string);
        }
        try{
            game.makeMove(move);
        } catch (InvalidMoveException e){
            String error_string = EscapeSequences.SET_TEXT_COLOR_RED + "The chess move is invalid. Try again";
            System.out.println(" I am here");
            throw new ResponseException(error_string);
        }
    }

    private String displayPiece(ChessPiece piece){   // Returns a string to represent the piece
        if(piece == null) return " \u2001 ";
        if(piece.getTeamColor() == ChessGame.TeamColor.WHITE){
            if(piece.getPieceType() == ChessPiece.PieceType.KING){
                return EscapeSequences.WHITE_KING;
            } else if(piece.getPieceType() == ChessPiece.PieceType.PAWN){
                return  EscapeSequences.WHITE_PAWN;
            } else if(piece.getPieceType() == ChessPiece.PieceType.ROOK){
                return  EscapeSequences.WHITE_ROOK;
            } else if(piece.getPieceType() == ChessPiece.PieceType.KNIGHT) {
                return EscapeSequences.WHITE_KNIGHT;
            } else if(piece.getPieceType() == ChessPiece.PieceType.BISHOP) {
                return EscapeSequences.WHITE_BISHOP;
            } else if(piece.getPieceType() == ChessPiece.PieceType.QUEEN) {
                return EscapeSequences.WHITE_QUEEN;
            }
        }
        if(piece.getTeamColor() == ChessGame.TeamColor.BLACK){
            if(piece.getPieceType() == ChessPiece.PieceType.KING){
                return EscapeSequences.BLACK_KING;
            } else if(piece.getPieceType() == ChessPiece.PieceType.PAWN){
                return  EscapeSequences.BLACK_PAWN;
            } else if(piece.getPieceType() == ChessPiece.PieceType.ROOK){
                return  EscapeSequences.BLACK_ROOK;
            } else if(piece.getPieceType() == ChessPiece.PieceType.KNIGHT) {
                return EscapeSequences.BLACK_KNIGHT;
            } else if(piece.getPieceType() == ChessPiece.PieceType.BISHOP) {
                return EscapeSequences.BLACK_BISHOP;
            } else if(piece.getPieceType() == ChessPiece.PieceType.QUEEN) {
                return EscapeSequences.BLACK_QUEEN;
            }
        }
        return null;
    }
    private void drawBlack(ChessGame game){
        ChessBoard board = game.getBoard();
        String terminalColor = "\u001B[0m";
        ChessPiece[][] squares = board.getSquares();

        String theString = EscapeSequences.SET_BG_COLOR_LIGHT_GREY + EscapeSequences.SET_TEXT_COLOR_BLACK;
        theString += "    h \u2001g \u2001f \u2001e \u2001d \u2001c \u2001b \u2001a  \u2001 " + terminalColor + "\n";
        for (int i=0; i<8; i++){  // Go through the rows
            theString += EscapeSequences.SET_BG_COLOR_LIGHT_GREY + EscapeSequences.SET_TEXT_COLOR_BLACK + " " + (i+1) + " ";
            for (int j=7; j>=0; j--){  // Go through the columns
                if ((i - j) % 2 == 0) theString += EscapeSequences.SET_BG_COLOR_MAGENTA;
                else theString += EscapeSequences.SET_BG_COLOR_WHITE;
                theString += displayPiece(squares[i][j]);
            }
            theString += EscapeSequences.SET_BG_COLOR_LIGHT_GREY + EscapeSequences.SET_TEXT_COLOR_BLACK + " " + (i+1) + " " + terminalColor;
            theString += "\n";  // Add new line
        }
        theString += EscapeSequences.SET_BG_COLOR_LIGHT_GREY + EscapeSequences.SET_TEXT_COLOR_BLACK;
        theString += "    h \u2001g \u2001f \u2001e \u2001d \u2001c \u2001b \u2001a  \u2001 " + terminalColor;
        System.out.println(theString);
    }
    private void drawWhite(ChessGame game){
        ChessBoard board = game.getBoard();
        String terminalColor = "\u001B[0m";
        ChessPiece[][] squares = board.getSquares();

        String theString = EscapeSequences.SET_BG_COLOR_LIGHT_GREY + EscapeSequences.SET_TEXT_COLOR_BLACK;
        theString += "    a \u2001b \u2001c \u2001d \u2001e \u2001f \u2001g \u2001h  \u2001 " + terminalColor + "\n";
        for (int i=7; i>=0; i--){  // Go through the rows
            theString += EscapeSequences.SET_BG_COLOR_LIGHT_GREY + EscapeSequences.SET_TEXT_COLOR_BLACK + " " + (i+1) + " ";
            for (int j=0; j<8; j++){  // Go through the columns
                if ((i - j) % 2 == 0) theString += EscapeSequences.SET_BG_COLOR_MAGENTA;
                else theString += EscapeSequences.SET_BG_COLOR_WHITE;
                theString += displayPiece(squares[i][j]);
            }
            theString += EscapeSequences.SET_BG_COLOR_LIGHT_GREY + EscapeSequences.SET_TEXT_COLOR_BLACK + " " + (i+1) + " " + terminalColor;
            theString += "\n";  // Add new line
        }
        theString += EscapeSequences.SET_BG_COLOR_LIGHT_GREY + EscapeSequences.SET_TEXT_COLOR_BLACK;
        theString += "    a \u2001b \u2001c \u2001d \u2001e \u2001f \u2001g \u2001h  \u2001 " + terminalColor;
        System.out.println(theString);
    }
    public void draw(ChessGame game){   // To display it correctly, used the monospaced setting
        if("black".equals(this.color)) drawBlack(game);
        else drawWhite(game);   // For both white user and observer
    }

    public void help() {
        // Create the string to help the user
        String helpString = EscapeSequences.SET_TEXT_COLOR_YELLOW;
        helpString += "redraw" + EscapeSequences.SET_TEXT_COLOR_WHITE;
        helpString += " - the current board\n";

        helpString += EscapeSequences.SET_TEXT_COLOR_YELLOW;
        helpString += "leave" + EscapeSequences.SET_TEXT_COLOR_WHITE;
        helpString += " - the game\n";

        helpString += EscapeSequences.SET_TEXT_COLOR_YELLOW;
        helpString += "move <position> <position>" + EscapeSequences.SET_TEXT_COLOR_WHITE;
        helpString += " - to make a move\n";

        helpString += EscapeSequences.SET_TEXT_COLOR_YELLOW;
        helpString += "resign" + EscapeSequences.SET_TEXT_COLOR_WHITE;
        helpString += " - the game\n";

        helpString += EscapeSequences.SET_TEXT_COLOR_YELLOW;
        helpString += "highlight" + EscapeSequences.SET_TEXT_COLOR_WHITE;
        helpString += " - legal moves\n";

        helpString += EscapeSequences.SET_TEXT_COLOR_YELLOW;
        helpString += "help" + EscapeSequences.SET_TEXT_COLOR_WHITE;
        helpString += " - with possible commands";

        System.out.println(helpString);
    }
}
