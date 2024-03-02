package dataAccess;

import model.GameData;

import java.sql.SQLException;
import java.util.Collection;

public class DatabaseGameDAO implements GameDAO{

    public DatabaseGameDAO() throws DataAccessException {
        configureDatabase();
    }
    @Override
    public void clear() throws DataAccessException {

    }

    @Override
    public Integer createGame(String gameName) throws DataAccessException {
        return null;
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

    // String for the SQL commands
    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS  gameDAO (
              `gameID` INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT,
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
