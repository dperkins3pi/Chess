package dataAccess;
import model.GameData;
import java.util.HashMap;
import java.util.Map;

public class MemoryGameDAO implements GameDAO{
    // Hash map of integers to GameData Objects
    private final Map<Integer, GameData> games = new HashMap<>();

    @Override
    public void clear() throws DataAccessException {
        games.clear();
    }

    @Override
    public void createGame() throws DataAccessException {

    }

    @Override
    public GameData getGame(Integer id) throws DataAccessException {
        return null;
    }

    @Override
    public Map<Integer, GameData> listGames() throws DataAccessException {
        return null;
    }

    @Override
    public void updateGame(Integer id) throws DataAccessException {

    }

}
