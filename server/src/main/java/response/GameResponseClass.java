package response;

import chess.ChessGame;
import com.google.gson.annotations.Expose;
import model.GameData;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public class GameResponseClass {
    String message;
    Collection<GameNameData> games;

    // Used to exclude the game from output on GameData
    private record GameNameData(int gameID, String whiteUsername, String blackUsername, String gameName){}

    public GameResponseClass(String message) {
        this.message = message;
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
