package server;
import com.google.gson.Gson;
import exception.ResponseException;
import java.io.*;
import java.net.*;
import request.*;
import response.GameResponseClass;
import response.ResponseClass;

public class ServerFacade {
    private final String serverUrl;

    public ServerFacade(String url) {
        serverUrl = url;
    }

    public ResponseClass login(String username, String password) throws ResponseException {
        var path = "/session";
        LoginRequest user = new LoginRequest(username, password);
        return this.makeRequest("POST", path, user, ResponseClass.class);
    }

    public ResponseClass register(String username, String password, String email) throws ResponseException {
        var path = "/user";
        RegisterRequest user = new RegisterRequest(username, password, email);
        return this.makeRequest("POST", path, user, ResponseClass.class);
    }

    public ResponseClass createGame(String authToken, String gameName) throws ResponseException {
        var path = "/game";
        CreateRequest game = new CreateRequest(authToken, gameName);
        return this.makeRequest("POST", path, game, ResponseClass.class);
    }

    public GameResponseClass listGames(String authToken) throws ResponseException {
        var path = "/game";
        AuthRequest token = new AuthRequest(authToken);
        return this.makeRequest("GET", path, token, GameResponseClass.class);
    }

    public void logOut(String authToken) throws ResponseException {
        var path = "/session";
        AuthRequest token = new AuthRequest(authToken);
        this.makeRequest("DELETE", path, token, GameResponseClass.class);
    }

    private <T> T makeRequest(String method, String path, Object request, Class<T> responseClass) throws ResponseException {
        try {
            URL url = (new URI(serverUrl + path)).toURL();
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod(method);
            http.setDoOutput(true);
            if(request instanceof CreateRequest) {  // If it has a header
                http.addRequestProperty("Authorization", ((CreateRequest) request).getAuthToken());
                ((CreateRequest) request).setAuthToken(null); // To exclude it from the body
            }
            if(request instanceof AuthRequest) {  // If it has a header
                http.setDoOutput(false);
                http.addRequestProperty("Authorization", ((AuthRequest) request).getAuthToken());
                ((AuthRequest) request).setAuthToken(null); // To exclude it from the body
            }
            else {
                writeBody(request, http);
            }
            http.connect();
            return readBody(http, responseClass);
        } catch (Exception ex) {
            throw new ResponseException(ex.getMessage());
        }
    }

    private <T> T readBody(HttpURLConnection http, Class<T> responseClass) throws Exception {
        T response = null;
        if (http.getContentLength() < 0) {
            try (InputStream respBody = http.getInputStream()) {  //Fails on get request!!!!!!
                InputStreamReader reader = new InputStreamReader(respBody);
                if (responseClass != null) {
                    response = new Gson().fromJson(reader, responseClass);
                }
            }
            catch (Exception e){  // To output the exception in a more understandable format
                InputStream inputStream = http.getErrorStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                String jsonString = reader.readLine();
                throw new Exception(jsonString);
            }
        }
        return response;
    }

    private static void writeBody(Object request, HttpURLConnection http) throws IOException {
        if (request != null) {
            http.addRequestProperty("Content-Type", "application/json");
            String reqData = new Gson().toJson(request);
            try (OutputStream reqBody = http.getOutputStream()) {
                reqBody.write(reqData.getBytes());
            }
        }
    }
}
