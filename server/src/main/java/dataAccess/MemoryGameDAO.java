package dataAccess;
import chess.ChessGame;
import model.GameData;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class MemoryGameDAO implements GameDAO{
    // Hash map of integers to GameData Objects
    private static final Map<Integer, GameData> games = new HashMap<>();
    private static int ID = 1;   // To remember ID

    @Override
    public void clear() throws DataAccessException {
        games.clear();
    }

    @Override
    public Integer createGame(String GameName) throws DataAccessException {
        ChessGame chessGame = new ChessGame();
        GameData game = new GameData(ID, "", "", GameName, chessGame);
        games.put(ID, game);
        int gameId = ID;
        ID = ID + 1;  // Increment ID
        return gameId;   // Return the ID of the game created
    }

    @Override
    public GameData getGame(Integer id) throws DataAccessException {
        return games.getOrDefault(id, null);
    }

    @Override
    public Collection<GameData> listGames() throws DataAccessException {
        return Collections.unmodifiableCollection(games.values());  // Returns the collection as unmodifiable
    }

    @Override
    public void updateGame(GameData game) throws DataAccessException {
        games.remove(game.gameID());  // Remove old game
        games.put(game.gameID(), game);  // Add the new updated game
    }

    @Override
    public Map<Integer, GameData> getGames(){
        return games;
    }
}
