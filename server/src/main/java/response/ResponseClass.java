package response;

public class ResponseClass {
    String username;
    String authToken;
    String message;

    public ResponseClass(String username, String authToken) {
        this.username = username;
        this.authToken = authToken;
    }

    public ResponseClass(String message) {
        this.message = message;
    }
}
