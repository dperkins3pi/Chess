package websocket;

import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.*;
import webSocketMessages.userCommands.UserGameCommand;

@WebSocket
public class WebSocketHandler {
    @OnWebSocketConnect
    public void onConnect(Session session){

    }
    @OnWebSocketClose
    public void onClose(Session session){

    }
    @OnWebSocketError
    public void onError(Session session){

    }

    @OnWebSocketMessage
    public void onMessage(Session session, String message){
        // Determine the message type
        UserGameCommand action = new Gson().fromJson(message, UserGameCommand.class);
        // Call one of the following method to process the message
        switch (action.getCommandType()) {
            case JOIN_PLAYER -> {;}
            case JOIN_OBSERVER -> {;}
            case MAKE_MOVE -> {;}
            case LEAVE -> {;}
            case RESIGN -> {;}
        }
    }

    public void sendMessage(Integer gameID, String message, String authToken){

    }
    public void broadcastMessage(Integer gameID, String message, String exceptThisAuthToken){

    }
}
