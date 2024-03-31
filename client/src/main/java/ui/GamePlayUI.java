package ui;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import com.google.gson.Gson;
import exception.ResponseException;
import handler.GameHandler;
import request.JoinGameOutput;
import webSocketMessages.serverMessages.ErrorMessage;
import webSocketMessages.serverMessages.LoadGameMessage;
import webSocketMessages.serverMessages.NotificationMessage;
import webSocketMessages.serverMessages.ServerMessage;

import java.util.Scanner;

public class GamePlayUI implements GameHandler {
    private final GamePlayClient client;
    private final String serverUrl;
    private final String authToken;
    public GamePlayUI(String serverUrl, String authToken, JoinGameOutput output) throws ResponseException {
        this.serverUrl = serverUrl;
        this.authToken = authToken;
        client = new GamePlayClient(serverUrl, authToken, this, output);
    }

    @Override
    public void updateGame(String message) {
        ServerMessage serverMessage = new Gson().fromJson(message, ServerMessage.class);
        if(serverMessage.getServerMessageType() == ServerMessage.ServerMessageType.LOAD_GAME){
            LoadGameMessage loadGameMessage = new LoadGameMessage(serverMessage);
            ChessGame game = loadGameMessage.getGame();
            Integer gameID = client.getGameID();
        }
    }

    @Override
    public void printMessage(String message) {
        ServerMessage serverMessage = new Gson().fromJson(message, ServerMessage.class);
        if(serverMessage.getServerMessageType() == ServerMessage.ServerMessageType.NOTIFICATION) {
            NotificationMessage notificationMessage = new NotificationMessage(serverMessage);
            String messageToPrint = notificationMessage.getMessage();
            if (messageToPrint != null) System.out.println(messageToPrint);
        } else if (serverMessage.getServerMessageType() == ServerMessage.ServerMessageType.LOAD_GAME) {
            LoadGameMessage loadGameMessage = new LoadGameMessage(serverMessage);
            client.draw(loadGameMessage.getGame());
        } else if (serverMessage.getServerMessageType() == ServerMessage.ServerMessageType.ERROR) {
            ErrorMessage errorMessage = new ErrorMessage(serverMessage);
            System.out.println(errorMessage.getErrorMessage());
        }
    }

    public void run() throws ResponseException {
        String state = "gamePlay";
        System.out.println(EscapeSequences.SET_TEXT_COLOR_YELLOW + EscapeSequences.WHITE_KING + EscapeSequences.SET_TEXT_COLOR_BLUE +
                " Type help if you need it. " +
                EscapeSequences.SET_TEXT_COLOR_YELLOW + EscapeSequences.WHITE_KING + EscapeSequences.SET_TEXT_COLOR_WHITE);
        Scanner scanner = new Scanner(System.in);
        String line = "";

        while (!line.equals("quit")) {  // Read input until the use quits
            System.out.println(EscapeSequences.SET_TEXT_COLOR_MAGENTA
                    + "[GAME_PLAY] >>> " + EscapeSequences.SET_TEXT_COLOR_WHITE);
            line = scanner.nextLine();
            try {
                state = client.eval(line);
                if (state.equals("loggedOut")) break;
                if (state.equals("loggedIn")) break;
            } catch (Throwable e) {
                var msg = e.getMessage();
                System.out.print(msg + "\n");
            }
        }
        if (!line.equals("quit") && state.equals("loggedOut")){  // If the user didn't quit, move to PreLoginUI
            new PreLoginUI(this.serverUrl).run();
        } else if (!line.equals("quit")){  // If the user didn't quit, move to PreLoginUI
            new PostLoginUI(this.serverUrl, authToken).run();
        }
    }
}
