package dataAccess;

import chess.ChessGame;
import exceptions.BadRequestException;
import model.GameData;
import com.google.gson.Gson;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class DatabaseGameDAO implements GameDAO{

    public DatabaseGameDAO() throws DataAccessException {
        configureDatabase();
    }
    @Override
    public void clear() throws DataAccessException {
        String sql = "DELETE FROM gameDAO";  // SQL command
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement(sql)) {
                preparedStatement.executeUpdate(); // Run the command
            }
        } catch (SQLException ex) {   // If the table was never created
            throw new DataAccessException(String.format("Unable to clear authToken: %s", ex.getMessage()));
        }
    }

    @Override
    public Integer createGame(String gameName) throws DataAccessException, BadRequestException {
        String sql = "INSERT INTO gameDAO (gameName, game) values (?, ?)";  // SQL command
        if(gameName == null){
            throw new BadRequestException("Invalid game name");
        }
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement(sql)) {
                preparedStatement.setString(1, gameName);
                preparedStatement.setString(2, new Gson().toJson(new ChessGame()));  // Convert the game to JSON
                preparedStatement.executeUpdate(); // Perform the statement
            }
        } catch (SQLException ex) {   // If the table was never created
            throw new DataAccessException(String.format("Unable to create game: %s", ex.getMessage()));
        }
        return getID(gameName);
    }

    @Override
    public GameData getGame(Integer id) throws DataAccessException {
        String whiteUsername = null;
        String blackUsername = null;
        String gameName = null;
        String gameString;
        ChessGame game = null;
        String sql = "SELECT gameID, whiteUsername, blackUsername, gameName, game FROM gameDAO WHERE gameID = ?";  // Select user
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement(sql)) {
                preparedStatement.setString(1, id.toString());
                ResultSet rs = preparedStatement.executeQuery();
                while(rs.next()){
                    whiteUsername = rs.getString("whiteUsername");
                    blackUsername = rs.getString("blackUsername");
                    gameName = rs.getString("gameName");  // Get the game name
                    gameString = rs.getString("game");  // Get the game as a JSON string
                    game = new Gson().fromJson(gameString, ChessGame.class);  // Create a Chess game instance
                }
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
        if (gameName == null) return null;   // If the game doesn't exist, return null
        return new GameData(id, whiteUsername, blackUsername, gameName, game);
    }

    @Override
    public Collection<GameData> listGames() {
        int gameID;
        String whiteUsername = null;
        String blackUsername = null;
        String gameName = null;
        String gameString;
        ChessGame game = null;
        Collection<GameData> games = new ArrayList<>();
        String sql = "SELECT gameID, whiteUsername, blackUsername, gameName, game FROM gameDAO";  // Select user
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement(sql)) {
                ResultSet rs = preparedStatement.executeQuery();
                while(rs.next()){
                    gameID = rs.getInt("gameID");
                    whiteUsername = rs.getString("whiteUsername");
                    blackUsername = rs.getString("blackUsername");
                    gameName = rs.getString("gameName");  // Get the game name
                    gameString = rs.getString("game");  // Get the game as a JSON string
                    game = new Gson().fromJson(gameString, ChessGame.class);  // Create a Chess game instance
                    games.add(new GameData(gameID, whiteUsername, blackUsername, gameName, game));  // Insert into map
                }
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
        return Collections.unmodifiableCollection(games);
    }

    @Override
    public void updateGame(GameData game) throws DataAccessException, BadRequestException {
        // Get the game data
        Integer id = game.gameID();
        if(getGame(id) == null) throw new BadRequestException("The game is not in the databse");
        String whiteUsername = game.whiteUsername();
        String blackUsername = game.blackUsername();
        String gameName = game.gameName();
        ChessGame theGame = game.game();

        String sql = "UPDATE gameDAO " +
                "SET whiteUsername = ?, blackUsername = ?, gameName = ?, game = ? " +
                "WHERE gameID = ?";
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement(sql)) {
                preparedStatement.setString(1, whiteUsername);
                preparedStatement.setString(2, blackUsername);
                preparedStatement.setString(3, gameName);
                preparedStatement.setString(4, new Gson().toJson(theGame));
                preparedStatement.setString(5, id.toString());
                preparedStatement.executeUpdate();  // Perform the update
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    private Integer getID(String gameName) throws DataAccessException {  // Gets the id of a certain game
        Integer id = null;
        String sql = "SELECT gameID FROM gameDAO WHERE gameName = ?";  // Select user
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement(sql)) {
                preparedStatement.setString(1, gameName);
                ResultSet rs = preparedStatement.executeQuery();
                while(rs.next()){
                    id = (Integer) rs.getObject("gameID");
                }
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
        return id;
    }

    public boolean isEmpty(){   // Checks to see if is empty
        String sql = "SELECT COUNT(*) from gameDAO";  // Counts the number of items in the DAO
        int rowCount = 0;
        try (var conn = DatabaseManager.getConnection()) {    //Error is here!!!!!!!!!
            try (var preparedStatement = conn.prepareStatement(sql)) {
                ResultSet rs = preparedStatement.executeQuery(sql);
                while(rs.next()){
                    rowCount = rs.getInt(1);
                }
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
        return rowCount == 0;
    }

    // String for the SQL commands
    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS  gameDAO (
              `gameID` INTEGER NOT NULL AUTO_INCREMENT,
              `whiteUsername` VARCHAR(256),
              `blackUsername` VARCHAR(256),
              `gameName` VARCHAR(256) NOT NULL,
              `game` TEXT NOT NULL,
              PRIMARY KEY (`gameID`)
            )
            """
    };
    private void configureDatabase() throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            for (var statement : createStatements) {
                try (var preparedStatement = conn.prepareStatement(statement)) {
                    preparedStatement.executeUpdate();
                }
            }
        } catch (SQLException ex) {   // If the table was never created
            throw new DataAccessException(String.format("Unable to configure database: %s", ex.getMessage()));
        }
    }
}
