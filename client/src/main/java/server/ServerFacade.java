package server;
import com.google.gson.Gson;
import exception.ResponseException;
import java.io.*;
import java.net.*;
import request.*;
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

    private <T> T makeRequest(String method, String path, Object request, Class<T> responseClass) throws ResponseException {
        try {
            URL url = (new URI(serverUrl + path)).toURL();
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod(method);
            http.setDoOutput(true);

            writeBody(request, http);
            http.connect();
            return readBody(http, responseClass);
        } catch (Exception ex) {
            throw new ResponseException(ex.getMessage());
        }
    }

    private <T> T readBody(HttpURLConnection http, Class<T> responseClass) throws IOException {
        T response = null;
        if (http.getContentLength() < 0) {
            try (InputStream respBody = http.getInputStream()) {
                InputStreamReader reader = new InputStreamReader(respBody);
                if (responseClass != null) {
                    response = new Gson().fromJson(reader, responseClass);
                }
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
