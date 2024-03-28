package websocket;

import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.*;
import webSocketMessages.userCommands.*;

@WebSocket
public class WebSocketHandler {
    WebSocketSessions sessions = new WebSocketSessions();
//    @OnWebSocketConnect
//    public void onConnect(Session session){}
//    @OnWebSocketClose
//    public void onClose(Session session){}
//    @OnWebSocketError
//    public void onError(Session session){}
    @OnWebSocketMessage
    public void onMessage(Session session, String message){
        // Determine the message type
        UserGameCommand action = new Gson().fromJson(message, UserGameCommand.class);
        // Call one of the following method to process the message
        // System.out.println("message received: " + action.getCommandType());
        switch (action.getCommandType()) {
            case JOIN_PLAYER -> {join(action, session);}
            case JOIN_OBSERVER -> {joinObserver(action, session);}
            case MAKE_MOVE -> {;}
            case LEAVE -> {leave(action, session);}
            case RESIGN -> {;}
        }
    }

    public void join(UserGameCommand action, Session session){
        JoinPlayerCommand joinAction = new JoinPlayerCommand(action);
        String authToken = joinAction.getAuthString();
        Integer gameID = joinAction.getGameID();
        sessions.addSessionToGame(gameID, authToken, session);
    }
    public void joinObserver(UserGameCommand action, Session session){
        JoinObserverCommand joinAction = new JoinObserverCommand(action);
        String authToken = joinAction.getAuthString();
        Integer gameID = joinAction.getGameID();
        sessions.addSessionToGame(gameID, authToken, session);
    }
    public void leave(UserGameCommand action, Session session){   //TODO: Make it actually remove the game from the DAO
        LeaveCommand leaveAction = new LeaveCommand(action);
        String authToken = leaveAction.getAuthString();
        Integer gameID = leaveAction.getGameID();
        sessions.removeSessionFromGame(gameID, authToken, session);
    }

    public void sendMessage(Integer gameID, String message, String authToken){

    }
    public void broadcastMessage(Integer gameID, String message, String exceptThisAuthToken){
//        var removeList = new ArrayList<Connection>();
//        for (var c : connections.values()) {
//            if (c.session.isOpen()) {
//                if (!c.visitorName.equals(excludeVisitorName)) {
//                    c.send(notification.toString());
//                }
//            } else {
//                removeList.add(c);
//            }
//        }
//
//        // Clean up any connections that were left open.
//        for (var c : removeList) {
//            connections.remove(c.visitorName);
//        }
    }

}
