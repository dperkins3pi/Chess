package server;

import javax.websocket.*;
import handler.GameHandler;


public class WebSocketFacade extends Endpoint {

    Session session;
    GameHandler gameHandler;

    //Endpoint Requires this method, but we will not use it
    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {;}
}
