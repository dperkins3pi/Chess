package server;

import javax.websocket.*;

import chess.ChessGame;
import chess.ChessMove;
import chess.InvalidMoveException;
import com.google.gson.Gson;
import handler.GameHandler;
import exception.ResponseException;
import ui.EscapeSequences;
import webSocketMessages.serverMessages.NotificationMessage;
import webSocketMessages.serverMessages.ServerMessage;
import webSocketMessages.userCommands.*;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;


public class WebSocketFacade extends Endpoint {

    Session session;
    GameHandler gameHandler;

    public WebSocketFacade(String url, GameHandler gamePlay) throws ResponseException {
        this.gameHandler = gamePlay;
        try {
            url = url.replace("http", "ws");
            URI socketURI = new URI(url + "/connect");

            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            this.session = container.connectToServer(this, socketURI);

            //set message handler
            this.session.addMessageHandler(new MessageHandler.Whole<String>() {
                @Override
                public void onMessage(String message) {
                    gameHandler.printMessage(message);
                }
            });
        } catch (DeploymentException | IOException | URISyntaxException ex) {
            throw new ResponseException(ex.getMessage());
        }
    }

    // Outgoing messages
    public void joinPlayer(String authToken, Integer gameID, String color) throws ResponseException {
        try {
            var action = new JoinPlayerCommand(authToken, gameID, color);
            this.session.getBasicRemote().sendText(new Gson().toJson(action));
        } catch (IOException ex) {
            throw new ResponseException(ex.getMessage());
        }
    }

    public void joinObserver(String authToken, Integer gameID) throws ResponseException {
        try {
            JoinObserverCommand action = new JoinObserverCommand(authToken, gameID);
            this.session.getBasicRemote().sendText(new Gson().toJson(action));
        } catch (IOException ex) {
            throw new ResponseException(ex.getMessage());
        }
    }

    public void leaveGame(String authToken, Integer gameID) throws ResponseException {
        try {
            var action = new LeaveCommand(authToken, gameID);
            this.session.getBasicRemote().sendText(new Gson().toJson(action));
        } catch (IOException ex) {
            throw new ResponseException(ex.getMessage());
        }

    }

    public void makeMove(String authToken, Integer gameID, ChessMove move) throws ResponseException {
        try{
            var action = new MakeMoveCommand(authToken, gameID, move);
            this.session.getBasicRemote().sendText(new Gson().toJson(action));
        } catch (IOException ex) {
            throw new ResponseException(ex.getMessage());
        }
    }

    public void resignGame(String authToken, Integer gameID) throws ResponseException {
        try {
            var action = new ResignCommand(authToken, gameID);
            this.session.getBasicRemote().sendText(new Gson().toJson(action));
        } catch (IOException ex) {
            throw new ResponseException(ex.getMessage());
        }
    }

    public void sendMessage(){

    }

    //Endpoint Requires this method, but we will not use it
    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {;}
}
