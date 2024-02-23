package dataAccess;
import model.GameData;

import java.util.Collection;
import java.util.Map;

public interface GameDAO {
    void clear() throws DataAccessException;
    Integer createGame(String gameName) throws DataAccessException;  // Create a game
    GameData getGame(Integer id) throws DataAccessException; // Retrieve a specified game with the given game ID.
    Collection<GameData> listGames() throws DataAccessException; // Retrieve all games.
    void updateGame(GameData game) throws DataAccessException;  // Updates a chess game. It should replace the chess game string corresponding to a given gameID. This is used when players join a game or when a move is made.
}
