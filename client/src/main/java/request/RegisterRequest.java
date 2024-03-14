package request;

public class RegisterRequest {
    String username;
    String password;
    String email;

    public String getUsername() {  // Used to parse in request object
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public RegisterRequest(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }
}
