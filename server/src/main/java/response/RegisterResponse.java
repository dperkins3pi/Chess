package response;

public class RegisterResponse {
    String username;
    String authToken;

    public RegisterResponse(String username, String authToken) {
        this.username = username;
        this.authToken = authToken;
    }
}
