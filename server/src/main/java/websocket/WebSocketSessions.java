package websocket;
import java.util.ArrayList;
import java.util.HashMap;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.*;
public class WebSocketSessions {
    HashMap<Integer, HashMap<String, Session>> sessionMap = new HashMap<>();
    // Maps gameID to Map<AuthToken, session>

    public void addSessionToGame(Integer gameID, String authToken, Session session){
        HashMap<String, Session> tokenToSession = new HashMap<>();
        tokenToSession.put(authToken, session);
        sessionMap.put(gameID, tokenToSession);
    }
    public void removeSessionFromGame(Integer gameID, String authToken, Session session){
        HashMap<String, Session> tokenToSession = sessionMap.get(gameID);  // Get part the we need to alter
        sessionMap.remove(gameID);  // Remove old one
        tokenToSession.remove(authToken, session);  // Alter the value to remove the specified session
        sessionMap.put(gameID, tokenToSession);  // Add altered one back
    }
    public void removeSession(Session session){
        for (HashMap<String, Session> tokenToSession : sessionMap.values()){
            tokenToSession.values().remove(session);   // Removes the
        }
    }

    public HashMap<String, Session> getSessionsForGame(Integer gameID){
        return sessionMap.get(gameID);
    }
}
