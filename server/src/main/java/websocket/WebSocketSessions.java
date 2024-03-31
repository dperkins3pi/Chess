package websocket;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.*;
public class WebSocketSessions {
    HashMap<Integer, HashMap<String, Session>> sessionMap = new HashMap<>();
    // Maps gameID to Map<AuthToken, session>

    public void addSessionToGame(Integer gameID, String authToken, Session session){
        HashMap<String, Session> tokenToSession = sessionMap.get(gameID);
        if(tokenToSession == null){
            tokenToSession = new HashMap<>();
        }
        tokenToSession.put(authToken, session);
        sessionMap.put(gameID, tokenToSession);
        System.out.println(sessionMap);

        int i = 1;
        for (HashMap<String, Session> tokenToSession2 : sessionMap.values()){
            System.out.print(i + ": ");
            for (Map.Entry<String, Session> entry : tokenToSession.entrySet()){
                System.out.println("auth" + entry.getKey());
            }
            i += 1;
        }
    }
    public void removeSessionFromGame(Integer gameID, String authToken, Session session){
        HashMap<String, Session> tokenToSession = sessionMap.get(gameID);  // Get part the we need to alter
        sessionMap.remove(gameID);  // Remove old one
        tokenToSession.remove(authToken, session);  // Alter the value to remove the specified session
        sessionMap.put(gameID, tokenToSession);  // Add altered one back

        int i = 1;
        for (HashMap<String, Session> tokenToSession2 : sessionMap.values()){
            System.out.print(i + ": ");
            for (Map.Entry<String, Session> entry : tokenToSession.entrySet()){
                System.out.println("auth" + entry.getKey());
            }
            i += 1;
        }
    }
    public void removeSession(Session session){
        Integer gameID = null;
        String authToken = null;
        for (Map.Entry<Integer, HashMap<String, Session>> bigMap : sessionMap.entrySet()){
            for (Map.Entry<String, Session> tokenToSession : bigMap.getValue().entrySet()){
                if(tokenToSession.getValue().equals(session)){
                    gameID = bigMap.getKey();
                    authToken = tokenToSession.getKey();
                    break;
                }
            }
            if(gameID != null) break;
        }
        if(gameID != null) removeSessionFromGame(gameID, authToken, session);
    }

    public String getAuth(Session session){
        System.out.println(sessionMap);
        for (HashMap<String, Session> tokenToSession : sessionMap.values()){
            for (Map.Entry<String, Session> entry : tokenToSession.entrySet()){
                if(entry.getValue() == session) {
                    return entry.getKey();
                }
            }
        }
        return null;  // No authToken corresponds to that session
    }

    public HashMap<String, Session> getSessionsForGame(Integer gameID){
        return sessionMap.get(gameID);
    }
}
