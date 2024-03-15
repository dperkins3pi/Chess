package request;

public class CreateRequest {
    String authToken;
    String gameName;

    public String getGameName() {  // Used to parse in request object
        return gameName;
    }

    public String getAuthToken() {  // Used to parse in request object
        return authToken;
    }

    public void setAuthToken(String authToken){
        this.authToken = authToken;
    }

    public CreateRequest(String authToken, String gameName) {
        this.authToken = authToken;
        this.gameName = gameName;
    }
}
