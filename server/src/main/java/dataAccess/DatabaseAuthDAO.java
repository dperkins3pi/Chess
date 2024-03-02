package dataAccess;

import model.AuthData;

import java.sql.SQLException;
import java.util.Map;

public class DatabaseAuthDAO implements AuthDAO{
    public DatabaseAuthDAO() throws DataAccessException {
        configureDatabase();
    }
    @Override
    public void clear() throws DataAccessException {

    }

    @Override
    public String createAuth(String username) throws DataAccessException {
        return null;
    }

    @Override
    public AuthData getAuth(String authToken) throws DataAccessException {
        return null;
    }

    @Override
    public String getUsername(String authToken) throws DataAccessException {
        return null;
    }

    @Override
    public void deleteAuth(String authToken) throws DataAccessException {

    }

    @Override
    public Map<String, AuthData> getAuthTokens() throws DataAccessException {
        return null;
    }

    // String for the SQL commands

//    """
//            CREATE TABLE IF NOT EXISTS  pet (
//              `id` int NOT NULL AUTO_INCREMENT,
//              `name` varchar(256) NOT NULL,
//              `type` ENUM('CAT', 'DOG', 'FISH', 'FROG', 'ROCK') DEFAULT 'CAT',
//              `json` TEXT DEFAULT NULL,
//              PRIMARY KEY (`id`),
//              INDEX(type),
//              INDEX(name)
//            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
//            """
    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS authDAO(
              `authToken` varchar(256) NOT NULL,
              `username` varchar(256) NOT NULL,
              PRIMARY KEY (`authToken`)
            )
            """
            // should I put    foreign key(username) references userDAO(username)   ?
    };
    private void configureDatabase() throws DataAccessException {
        DatabaseManager.createDatabase();
        try (var conn = DatabaseManager.getConnection()) {    //Error is here!!!!!!!!!
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
