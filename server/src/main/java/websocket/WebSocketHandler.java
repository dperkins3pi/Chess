package websocket;

import com.google.gson.Gson;
import exceptions.BadRequestException;
import exceptions.UnauthorizedException;
import model.GameData;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.*;
import webSocketMessages.userCommands.*;
import dataAccess.*;
import java.io.IOException;
import java.util.HashMap;

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
            case MAKE_MOVE -> {;}
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
        // TODO Messages not being broadcast!!!!!!!
        String message = " joined the game as " + color;
        this.broadcastMessage(gameID, message, authToken);
    }
    public void joinObserver(UserGameCommand action, Session session) throws IOException {
        JoinObserverCommand joinAction = new JoinObserverCommand(action);
        String authToken = joinAction.getAuthString();
        Integer gameID = joinAction.getGameID();
        sessions.addSessionToGame(gameID, authToken, session);
        // TODO Messages not being broadcast!!!!!!!
        String message = " joined the game as an observer";
        this.broadcastMessage(gameID, message, authToken);
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
            if (white.equals(username)) white = null;   // Determine which user to remove
            if (black.equals(username)) black = null;
            GameData updatedGame = new GameData(game.gameID(), white, black, game.gameName(), game.game());
            gameDao.updateGame(updatedGame);
        } catch (DataAccessException | BadRequestException | UnauthorizedException e) {
            throw new RuntimeException(e);
        }

        // TODO Messages not being broadcast!!!!!!!
        String message = " left the game";
        this.broadcastMessage(gameID, message, authToken);
    }

    public void sendMessage(Integer gameID, String message, String authToken){

    }
    public void broadcastMessage(Integer gameID, String message, String exceptThisAuthToken) throws IOException {
        HashMap<String, Session> theSessions = sessions.getSessionsForGame(gameID);
        Session exceptThisSession = theSessions.get(exceptThisAuthToken);
        for (var c : theSessions.values()) {
            if (c.isOpen()) {
                if (!c.equals(exceptThisSession)) {
                    c.getRemote().sendString(message);
                }
            } else {
                c.disconnect();    // Clean up any connections that were left open.   //TODO: Remove user from game
            }
        }
    }

}
