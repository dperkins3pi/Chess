package websocket;

import chess.*;
import com.google.gson.Gson;
import exceptions.BadRequestException;
import exceptions.UnauthorizedException;
import model.GameData;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.*;
import webSocketMessages.serverMessages.ErrorMessage;
import webSocketMessages.serverMessages.LoadGameMessage;
import webSocketMessages.serverMessages.NotificationMessage;
import webSocketMessages.userCommands.*;
import dataAccess.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@WebSocket
public class WebSocketHandler {
    WebSocketSessions sessions = new WebSocketSessions();
    DatabaseGameDAO gameDao;
    DatabaseAuthDAO authDAO;
    {
        try {
            gameDao = new DatabaseGameDAO();
            authDAO = new DatabaseAuthDAO();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    //    @OnWebSocketConnect
//    public void onConnect(Session session){}
//    @OnWebSocketClose
//    public void onClose(Session session){}
//    @OnWebSocketError
//    public void onError(Session session){}
    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws IOException {
        // Determine the message type
        UserGameCommand action = new Gson().fromJson(message, UserGameCommand.class);

        // System.out.println("message received: " + action.getCommandType());
        // Call one of the following method to process the message
        switch (action.getCommandType()) {
            case JOIN_PLAYER -> {join(action, session);}
            case JOIN_OBSERVER -> {joinObserver(action, session);}
            case MAKE_MOVE -> {makeMove(action, session);}
            case LEAVE -> {leave(action, session);}
            case RESIGN -> {resign(action, session);}
        }
    }

    public void join(UserGameCommand action, Session session) throws IOException {
        JoinPlayerCommand joinAction = new JoinPlayerCommand(action);
        String authToken = joinAction.getAuthString();
        Integer gameID = joinAction.getGameID();
        String color = joinAction.getTeamColor();
        boolean worked = true;

        sessions.addSessionToGame(gameID, authToken, session);
        String username;
        ChessGame game;
        try {
            username = authDAO.getUsername(authToken);
            try {
                game = gameDao.getGame(gameID).game();
            } catch (Exception e) {
                String jsonMessage2;
                jsonMessage2 = new Gson().toJson(new ErrorMessage("Error: The game doesn't exits"));
                this.sendMessage(jsonMessage2, session);
                return;
            }

            if (gameDao.getGame(gameID).blackUsername() == null && gameDao.getGame(gameID).whiteUsername() == null) worked = false;
            if("white".equalsIgnoreCase(color) && gameDao.getGame(gameID).whiteUsername() != null
                    && !Objects.equals(gameDao.getGame(gameID).whiteUsername(), username)) worked = false;
            if("black".equalsIgnoreCase(color) && gameDao.getGame(gameID).blackUsername() != null
                    && !Objects.equals(gameDao.getGame(gameID).blackUsername(), username)) worked = false;

        } catch (DataAccessException | UnauthorizedException e) {
            String jsonMessage2;
            jsonMessage2 = new Gson().toJson(new ErrorMessage("Error: The user doesn't exits"));
            this.sendMessage(jsonMessage2, session);
            throw new RuntimeException(e);

        }
        if(worked) {
            String message = username + " joined the game as " + color;
            String jsonMessage = new Gson().toJson(new NotificationMessage(message));
            this.broadcastMessage(gameID, jsonMessage, authToken);

            String jsonMessage2;
            jsonMessage2 = new Gson().toJson(new LoadGameMessage(game));
            this.sendMessage(jsonMessage2, session);
        }
        else{
            String jsonMessage2;
            jsonMessage2 = new Gson().toJson(new ErrorMessage("Error: The player is already taken"));
            this.sendMessage(jsonMessage2, session);
        }
    }

    public void joinObserver(UserGameCommand action, Session session) throws IOException {
        JoinObserverCommand joinAction = new JoinObserverCommand(action);
        String authToken = joinAction.getAuthString();
        Integer gameID = joinAction.getGameID();
        sessions.addSessionToGame(gameID, authToken, session);
        String username = null;
        ChessGame game = null;
        try {
            username = authDAO.getUsername(authToken);
            try {
                game = gameDao.getGame(gameID).game();
            } catch (Exception e){
                String jsonMessage2;
                jsonMessage2 = new Gson().toJson(new ErrorMessage("Error: The game doesn't exits"));
                this.sendMessage(jsonMessage2, session);
                return;
            }
        } catch (DataAccessException | UnauthorizedException e) {
            String jsonMessage2;
            jsonMessage2 = new Gson().toJson(new ErrorMessage("Error: The user doesn't exits"));
            this.sendMessage(jsonMessage2, session);
            throw new RuntimeException(e);
        }

        String message = username + " joined the game as an observer";
        String jsonMessage = new Gson().toJson(new NotificationMessage(message));
        this.broadcastMessage(gameID, jsonMessage, authToken);

        String jsonMessage2 = new Gson().toJson(new LoadGameMessage(game));
        this.sendMessage(jsonMessage2, session);
    }
    public void leave(UserGameCommand action, Session session) throws IOException {
        LeaveCommand leaveAction = new LeaveCommand(action);
        String authToken = leaveAction.getAuthString();
        Integer gameID = leaveAction.getGameID();

        // Remove the game form sessions
        sessions.removeSessionFromGame(gameID, authToken, session);
        // Remove user from the game in the DAO by updating the current game in the DAO
        GameData game = null;
        try {
            game = gameDao.getGame(gameID);
            String username = authDAO.getUsername(authToken);
            String white = game.whiteUsername();
            String black = game.blackUsername();
            if(username != null) {
                if (username.equals(white)) white = null;   // Determine which user to remove
                if (username.equals(black)) black = null;
            }
            GameData updatedGame = new GameData(game.gameID(), white, black, game.gameName(), game.game());
            gameDao.updateGame(updatedGame);
        } catch (DataAccessException | BadRequestException | UnauthorizedException e) {
            throw new RuntimeException(e);
        }

        String username;
        try {
            username = authDAO.getUsername(authToken);
        } catch (DataAccessException | UnauthorizedException e) {
            throw new RuntimeException(e);
        }

        String message = username + " left the game";
        String jsonMessage = new Gson().toJson(new NotificationMessage(message));
        this.broadcastMessage(gameID, jsonMessage, authToken);
    }

    public void resign(UserGameCommand action, Session session) throws IOException {
        ResignCommand resignCommand = new ResignCommand(action);
        String authToken = resignCommand.getAuthString();
        Integer gameID = resignCommand.getGameID();
        boolean worked = true;

        String username;
        try {
            username = authDAO.getUsername(authToken);
            ChessGame game = gameDao.getGame(gameID).game();
            String gameName = gameDao.getGame(gameID).gameName();
            String white = gameDao.getGame(gameID).whiteUsername();
            String black = gameDao.getGame(gameID).blackUsername();

            if(!username.equalsIgnoreCase(black) && !username.equalsIgnoreCase(white)){
                worked = false;
            }
            if(game.getTeamTurn() == ChessGame.TeamColor.COMPLETE){   // The game can only be resigned once
                worked = false;
            }

            game.setTeamTurn(ChessGame.TeamColor.COMPLETE);  // No one can move anymore by making the team color null
            GameData updatedGame = new GameData(gameID, white, black, gameName, game);
            gameDao.updateGame(updatedGame);
        } catch (DataAccessException | UnauthorizedException | BadRequestException e) {
            throw new RuntimeException(e);
        }

        if(worked) {
            String message = username + " resigned. The game is now over.";
            String jsonMessage = new Gson().toJson(new NotificationMessage(message));
            this.broadcastMessage(gameID, jsonMessage, authToken);

            String message2 = " You have resigned. The game is now over.";
            String jsonMessage2 = new Gson().toJson(new NotificationMessage(message2));
            this.sendMessage(jsonMessage2, session);
        } else{
            String message2 = " Error: You can only resign as a player for a game that isn't finished.";
            String jsonMessage2 = new Gson().toJson(new ErrorMessage(message2));
            this.sendMessage(jsonMessage2, session);
        }
    }
    public void makeMove(UserGameCommand action, Session session) throws IOException {

        MakeMoveCommand moveAction = new MakeMoveCommand(action);
        String authToken = moveAction.getAuthString();
        Integer gameID = moveAction.getGameID();
        ChessMove move = moveAction.getMove();
        String username = null;
        ChessGame game = null;
        String playerColor = null;
        boolean worked = true;

        try {
            username = authDAO.getUsername(authToken);
            game = gameDao.getGame(gameID).game();

            if(gameDao.getGame(gameID).whiteUsername().equals(username)) {
                if(gameDao.getGame(gameID).blackUsername().equals(username)) playerColor = "both";  // Rare case that player is on both sides
                else playerColor = "white";
            } else if (gameDao.getGame(gameID).blackUsername().equals(username)) {
                playerColor = "black";
            }

            ChessBoard board = game.getBoard();
            ChessPiece piece = board.getPiece(move.getStartPosition());

            if(piece == null || !piece.getTeamColor().toString().equals(game.getTeamTurn().toString())){
                String errorString = "Invalid positions given.\n" +
                        "The starting position must be on a piece of your team";
                throw new InvalidMoveException(errorString);
            } else if (!piece.getTeamColor().toString().equalsIgnoreCase(playerColor)) {
                if(!piece.getTeamColor().toString().equalsIgnoreCase("both")){
                    String errorString = "Invalid move attempt.\n" +
                            "You can only move pieces of your color";
                    throw new InvalidMoveException(errorString);
                }
            }

            game.makeMove(move);
            GameData oldGameData = gameDao.getGame(gameID);
            GameData newGameData = new GameData(gameID, oldGameData.whiteUsername(), oldGameData.blackUsername(),
                    oldGameData.gameName(), game);
            gameDao.updateGame(newGameData);
        } catch (InvalidMoveException e){
            worked = false;
        } catch (DataAccessException | UnauthorizedException | BadRequestException e) {
            throw new RuntimeException(e);
        }

        // Send messages
        if(worked) {
            String message = username + " moved his " + game.getBoard().getPiece(move.getEndPosition());
            String jsonMessage = new Gson().toJson(new NotificationMessage(message));
            this.broadcastMessage(gameID, jsonMessage, authToken);

            String jsonMessage2 = new Gson().toJson(new LoadGameMessage(game));
            this.sendMessage(jsonMessage2, session);
            this.broadcastMessage(gameID, jsonMessage2, authToken);
        }
        else{   // An error was thrown
            String errorMessage = "Invalid Move and/or Incorrect Turn.\nPlease try again or wait for your turn.";
            String jsonMessage = new Gson().toJson(new ErrorMessage(errorMessage));
            this.sendMessage(jsonMessage, session);
        }
        //Note: Check mate is handled in shared
    }

    public void sendMessage(String message, Session session) throws IOException {
        session.getRemote().sendString(message);
    }
    public void broadcastMessage(Integer gameID, String message, String exceptThisAuthToken) throws IOException {
        HashMap<String, Session> theSessions = sessions.getSessionsForGame(gameID);
        Session exceptThisSession = theSessions.get(exceptThisAuthToken);
        for (Map.Entry<String, Session> entry: theSessions.entrySet()) {
            String authToken = entry.getKey();
            Session c = entry.getValue();
            if (c.isOpen()) {
                if (!c.equals(exceptThisSession)) {
                    c.getRemote().sendString(message);
                }
            } else {  // User no longer active, so he/she will leave the game
                LeaveCommand action = new LeaveCommand(authToken, gameID);
                leave(action, c);   // Remove the user from the game
                sessions.removeSession(c);
            }
        }
    }

}
