package dataAccess;
import chess.ChessGame;
import model.GameData;
import java.util.HashMap;
import java.util.Map;

public class MemoryGameDAO implements GameDAO{
    // Hash map of integers to GameData Objects
    private static final Map<Integer, GameData> games = new HashMap<>();
    private static int ID = 0;   // To remember ID

    @Override
    public void clear() throws DataAccessException {
        games.clear();
    }

    @Override
    public void createGame() throws DataAccessException {
        ChessGame chessGame = new ChessGame();
        GameData game = new GameData(ID, "", "", "", chessGame);
        games.put(ID, game);
        ID = ID + 1;  // Increment ID
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

    @Override
    public Map<Integer, GameData> getGames(){
        return games;
    }
}
