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
            case RESIGN -> {;}
        }
    }

    public void join(UserGameCommand action, Session session) throws IOException {
        JoinPlayerCommand joinAction = new JoinPlayerCommand(action);
        String authToken = joinAction.getAuthString();
        Integer gameID = joinAction.getGameID();
        String color = joinAction.getTeamColor();
        sessions.addSessionToGame(gameID, authToken, session);
        String username = null;
        ChessGame game = null;
        try {
            username = authDAO.getUsername(authToken);
            game = gameDao.getGame(gameID).game();
        } catch (DataAccessException | UnauthorizedException e) {
            throw new RuntimeException(e);
        }
        String message = username + " joined the game as " + color;
        String JSONMessage = new Gson().toJson(new NotificationMessage(message));
        this.broadcastMessage(gameID, JSONMessage, authToken);

        String JSONMessage2;
        if(gameID == null) {  // An error occured so we pass in null
            JSONMessage2 = new Gson().toJson(new ErrorMessage("An Error Occurred"));
        }
        else{
            JSONMessage2 = new Gson().toJson(new LoadGameMessage(game));
        }
        this.sendMessage(JSONMessage2, session);
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
            game = gameDao.getGame(gameID).game();
        } catch (DataAccessException | UnauthorizedException e) {
            throw new RuntimeException(e);
        }

        String message = username + " joined the game as an observer";
        String JSONMessage = new Gson().toJson(new NotificationMessage(message));
        this.broadcastMessage(gameID, JSONMessage, authToken);

        String JSONMessage2 = new Gson().toJson(new LoadGameMessage(game));
        this.sendMessage(JSONMessage2, session);
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

        String username = null;
        try {
            username = authDAO.getUsername(authToken);
        } catch (DataAccessException | UnauthorizedException e) {
            throw new RuntimeException(e);
        }

        String message = username + " left the game";
        String JSONMessage = new Gson().toJson(new NotificationMessage(message));
        this.broadcastMessage(gameID, JSONMessage, authToken);
    }

    public void makeMove(UserGameCommand action, Session session) throws IOException {

        MakeMoveCommand moveAction = new MakeMoveCommand(action);
        String authToken = moveAction.getAuthString();
        Integer gameID = moveAction.getGameID();
        ChessMove move = moveAction.getMove();
        String username = null;
        ChessGame game = null;

        try {
            username = authDAO.getUsername(authToken);
            game = gameDao.getGame(gameID).game();
            game.makeMove(move);
            GameData oldGameData = gameDao.getGame(gameID);
            GameData newGameData = new GameData(gameID, oldGameData.whiteUsername(), oldGameData.blackUsername(),
                    oldGameData.gameName(), game);
            gameDao.updateGame(newGameData);
        } catch (InvalidMoveException e){
            System.out.println("An error was thrown");   //TODO: Actually send an error message
        } catch (DataAccessException | UnauthorizedException | BadRequestException e) {
            throw new RuntimeException(e);
        }

        // Send messages
        String message = username + " moved his " + game.getBoard().getPiece(move.getEndPosition());
        String JSONMessage = new Gson().toJson(new NotificationMessage(message));
        this.broadcastMessage(gameID, JSONMessage, authToken);

        String JSONMessage2 = new Gson().toJson(new LoadGameMessage(game));
        this.sendMessage(JSONMessage2, session);
        this.broadcastMessage(gameID, JSONMessage2, authToken);
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
