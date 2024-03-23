package websocket;
import java.util.HashMap;

import javax.websocket.*;
public class WebSocketSessions {
    HashMap<Integer, HashMap<String, Session>> sessionMap;
    // Maps gameID to Map<AuthToken, session>

    public void addSessionToGame(Integer gameID, String authToken, Session session){

    }
    public void removeSessionFromGame(Integer gameID, String authToken, Session session){

    }
    public void removeSession(Session session){

    }

    public HashMap<String, Session> getSessionsForGame(Integer gameID){
        return null;
    }
}
