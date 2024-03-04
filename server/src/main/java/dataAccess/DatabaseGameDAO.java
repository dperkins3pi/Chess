package dataAccess;

import exceptions.AlreadyTakenException;
import exceptions.BadRequestException;
import model.GameData;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.UUID;

public class DatabaseGameDAO implements GameDAO{

    private int id = 0;

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
        String sql = "INSERT INTO gameDAO (whiteUsername, blackUsername, gameName, game) values (?, ?, ?, ?)";  // SQL command
        if(gameName == null){
            throw new BadRequestException("Invalid game name");
        }
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement(sql)) {
                preparedStatement.setString(1, "");   // Get the data
                preparedStatement.setString(2, "");
                preparedStatement.setString(3, gameName);
                preparedStatement.setString(4, "");
                preparedStatement.executeUpdate(); // Perform the statement
            }
        } catch (SQLException ex) {   // If the table was never created
            throw new DataAccessException(String.format("Unable to create game: %s", ex.getMessage()));
        }
        id += 1;
        return id;
    }

    @Override
    public GameData getGame(Integer id) throws DataAccessException {
        return null;
    }

    @Override
    public Collection<GameData> listGames() throws DataAccessException {
        return null;
    }

    @Override
    public void updateGame(GameData game) throws DataAccessException {

    }

    public boolean isEmpty(){   // Checks to see if is empty
        String sql = "SELECT COUNT(*) from gameDAO";  // Counts the number of items in the DAO
        int row_count = 0;
        try (var conn = DatabaseManager.getConnection()) {    //Error is here!!!!!!!!!
            try (var preparedStatement = conn.prepareStatement(sql)) {
                ResultSet rs = preparedStatement.executeQuery(sql);
                while(rs.next()){
                    row_count = rs.getInt(1);
                }
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
        return row_count == 0;
    }

    // String for the SQL commands
    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS  gameDAO (
              `gameID` INTEGER NOT NULL AUTO_INCREMENT,
              `whiteUsername` VARCHAR(256),
              `blackUsername` VARCHAR(256),
              `gameName` VARCHAR(256) NOT NULL,
              `game` BLOB NOT NULL,
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
