package request;

public class CreateGameRequest {
    String gameName;

    public String getGameName() {
        return gameName;
    }

    public CreateGameRequest(String gameName) {
        this.gameName = gameName;
    }
}
