package request;

public class CreateGameRequest { // Used to parse in request object
    String gameName;

    public String getGameName() {
        return gameName;
    }

    public CreateGameRequest(String gameName) {
        this.gameName = gameName;
    }
}
