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
    private static int id = 1;   // To remember ID

    @Override
    public void clear() throws DataAccessException {
        games.clear();
    }

    @Override
    public Integer createGame(String gameName) throws DataAccessException {
        try {
            ChessGame chessGame = new ChessGame();
            GameData game = new GameData(id, null, null, gameName, chessGame);
            games.put(id, game);
            int gameId = id;
            id = id + 1;  // Increment ID
            return gameId;   // Return the ID of the game created
        } catch (Exception e){
            throw new DataAccessException("Data Access Exception");
        }
    }

    @Override
    public GameData getGame(Integer id) throws DataAccessException {
        try {
            return games.getOrDefault(id, null);
        } catch (Exception e){
            throw new DataAccessException("Data Access Exception");
        }
    }

    @Override
    public Collection<GameData> listGames() throws DataAccessException {
        try {
            return Collections.unmodifiableCollection(games.values());  // Returns the collection as unmodifiable
        } catch (Exception e){
            throw new DataAccessException("Data Access Exception");
        }
    }

    @Override
    public void updateGame(GameData game) throws DataAccessException {
        try {
            games.remove(game.gameID());  // Remove old game
            games.put(game.gameID(), game);  // Add the new updated games
        } catch (Exception e){
            throw new DataAccessException("Data Access Exception");
        }
    }

    @Override
    public Map<Integer, GameData> getGames() throws DataAccessException {
        try {
            return games;
        } catch (Exception e){
            throw new DataAccessException("Data Access Exception");
        }
    }
}
