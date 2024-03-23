package server;

import javax.websocket.*;

import com.google.gson.Gson;
import handler.GameHandler;
import exception.ResponseException;
import webSocketMessages.userCommands.*;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;


public class WebSocketFacade extends Endpoint {

    Session session;
    GameHandler gameHandler;

    public WebSocketFacade(String url) throws ResponseException {
        try {
            url = url.replace("http", "ws");
            URI socketURI = new URI(url + "/connect");

            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            this.session = container.connectToServer(this, socketURI);

            //set message handler
            this.session.addMessageHandler(new MessageHandler.Whole<String>() {
                @Override
                public void onMessage(String message) {
                    System.out.println(message);
                }
            });
        } catch (DeploymentException | IOException | URISyntaxException ex) {
            throw new ResponseException(ex.getMessage());
        }
    }

    // Outgoing messages
    public void joinPlayer() {
    }

    public void joinObserver() {
    }

    public void makeMove() {
    }

    public void leaveGame(String authToken, Integer gameID) throws ResponseException {
        try {
            var action = new LeaveCommand(authToken, gameID);
            this.session.getBasicRemote().sendText(new Gson().toJson(action));
        } catch (IOException ex) {
            throw new ResponseException(ex.getMessage());
        }
    }

    public void resignGame() {
    }

    public void sendMessage(){

    }

    // Process incoming messages
    public void onMessage(String message){
        // Deserialize the message
        // Call game handler to process the message
    }

    //Endpoint Requires this method, but we will not use it
    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {;}
}
