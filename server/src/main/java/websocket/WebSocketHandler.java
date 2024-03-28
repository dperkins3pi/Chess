package websocket;

import chess.ChessBoard;
import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.*;
import service.JoinGameService;
import webSocketMessages.userCommands.LeaveCommand;
import webSocketMessages.userCommands.UserGameCommand;

@WebSocket
public class WebSocketHandler {

//    @OnWebSocketConnect
//    public void onConnect(Session session){
//
//    }
//    @OnWebSocketClose
//    public void onClose(Session session){
//
//    }
//    @OnWebSocketError
//    public void onError(Session session){
//
//    }
    @OnWebSocketMessage
    public void onMessage(Session session, String message){
        // Determine the message type
        UserGameCommand action = new Gson().fromJson(message, UserGameCommand.class);
        // Call one of the following method to process the message
        switch (action.getCommandType()) {
            case JOIN_PLAYER -> {;}
            case JOIN_OBSERVER -> {;}
            case MAKE_MOVE -> {;}
            case LEAVE -> {leave(action);}
            case RESIGN -> {;}
        }
    }

    public void leave(UserGameCommand action){
        LeaveCommand leaveAction = (LeaveCommand) action;
        String authToken = action.getAuthString();
    }

    public void sendMessage(Integer gameID, String message, String authToken){

    }
    public void broadcastMessage(Integer gameID, String message, String exceptThisAuthToken){

//        public Connection(String visitorName, Session session) {
//            this.visitorName = visitorName;
//            this.session = session;
//        }
//
//        public void send(String msg) throws IOException {
//            session.getRemote().sendString(msg);
//        }


    }


//    public void broadcast(String excludeVisitorName, Notification notification) throws IOException {
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
//    }
}
