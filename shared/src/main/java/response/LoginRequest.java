package response;

public class LoginRequest {
    String username;
    String password;

    public String getUsername() {  // Used to parse in request object
        return username;
    }

    public String getPassword() {
        return password;
    }

    public LoginRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
