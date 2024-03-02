package dataAccess;

import model.UserData;

import java.sql.SQLException;

public class DatabaseUserDAO implements UserDAO{
    public DatabaseUserDAO() throws DataAccessException {
        configureDatabase();
    }
    @Override
    public void clear() throws DataAccessException {

    }

    @Override
    public void createUser(String username, String password, String email) throws DataAccessException {

    }

    @Override
    public UserData getUser(String username) throws DataAccessException {
        return null;
    }

    // String for the SQL commands
    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS  authDAO (
              `username` varchar(256) NOT NULL,
              `password` varchar(256) NOT NULL,
              `email` varchar(256) NOT NULL,
              PRIMARY KEY (`username`)
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
