package response;
import model.GameData;

import java.util.ArrayList;
import java.util.Collection;

public class GameResponseClass {
    String message;
    Collection<GameNameData> games;

    // Used to exclude the game from output on GameData
    private record GameNameData(int gameID, String whiteUsername, String blackUsername, String gameName){}

    public GameResponseClass(String message) {
        this.message = message;
    }

    public Collection<GameNameData> getGames() {
        return games;
    }

    public Boolean Equals(Collection<GameData> games){
        var gameNames = new ArrayList<GameNameData>();
        for(GameData game:games){   //Iterate through each game and save data
            int gameID = game.gameID();
            String whiteUsername = game.whiteUsername();
            String blackUsername = game.blackUsername();
            String gameName = game.gameName();
            if(!this.games.contains(new GameNameData(gameID, whiteUsername, blackUsername, gameName))) return false;
        }
        return true;
    }

    public Integer getID(int id){
        for (var game : games){   // Converts the id the user gives to the real id stored in the DAO
            return game.gameID() + id - 1;
        }
        return null;
    }

    public void printGames(){
        int i = 1;  // For counting the games
        for (var game : games){
            String toPrint = "#" + i + ": " + game.gameName();
            toPrint += ", White Username: " + game.whiteUsername();
            toPrint += ", Black Username: " + game.blackUsername();
            toPrint += ", ID: " + String.valueOf(game.gameID());
            System.out.println(toPrint);
            i += 1;
        }
    }

    public GameResponseClass(Collection<GameData> games){
        var gameNames = new ArrayList<GameNameData>();
        for(GameData game:games){   //Iterate through each game and save data
            int gameID = game.gameID();
            String whiteUsername = game.whiteUsername();
            String blackUsername = game.blackUsername();
            String gameName = game.gameName();
            gameNames.add(new GameNameData(gameID, whiteUsername, blackUsername, gameName));
        }
        this.games = gameNames;
    }
}
