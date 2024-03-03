package dataAccess;

import exceptions.AlreadyTakenException;
import model.AuthData;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.UUID;

public class DatabaseAuthDAO implements AuthDAO{
    public DatabaseAuthDAO() throws DataAccessException {
        configureDatabase();
    }
    @Override
    public void clear() throws DataAccessException {
        String sql = "DELETE FROM AuthDAO";  // SQL command
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement(sql)) {
                preparedStatement.executeUpdate(); // Run the commnand
            }
        } catch (SQLException ex) {   // If the table was never created
            throw new DataAccessException(String.format("Unable to clear authToken: %s", ex.getMessage()));
        }
    }

    @Override
    public String createAuth(String username) throws DataAccessException, AlreadyTakenException {
        String newToken = UUID.randomUUID().toString();  // Create authToken

        String sql = "INSERT INTO AuthDAO (authToken, username) values (?, ?)";  // SQL command
        if(contains(username)){
            throw new AlreadyTakenException("The username is already taken");
        }
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement(sql)) {
                preparedStatement.setString(1, newToken);   // Get the data
                preparedStatement.setString(2, username);
                preparedStatement.executeUpdate(); // Perform the statement
            }
        } catch (SQLException ex) {   // If the table was never created
            throw new DataAccessException(String.format("Unable to create authToken: %s", ex.getMessage()));
        }
        return newToken;
    }

    public boolean contains(String username){   // Sees if the username already in DAO
        String sql = "SELECT username FROM AuthDAO WHERE username = ?";  // SQL command
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement(sql)) {
                preparedStatement.setString(1, username);   // Get the data
                ResultSet rs = preparedStatement.executeQuery(); // Perform the statement
                if(rs.next()) return true;  // Get the username
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
        return false;
    }

    @Override
    public AuthData getAuth(String authToken) throws DataAccessException {
        String username = null;
        String sql = "SELECT username FROM AuthDAO WHERE authToken = ?";  // Counts the number of items in the DAO
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement(sql)) {
                preparedStatement.setString(1, authToken);
                ResultSet rs = preparedStatement.executeQuery();
                while(rs.next()){
                    username = rs.getObject("username").toString();  // Get the username
                }
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
        return new AuthData(authToken, username);
    }

    @Override
    public String getUsername(String authToken) throws DataAccessException {
        String username = null;
        String sql = "SELECT username FROM AuthDAO WHERE authToken = ?";  // Select user
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement(sql)) {
                preparedStatement.setString(1, authToken);
                ResultSet rs = preparedStatement.executeQuery();
                while(rs.next()){
                    username = rs.getObject("username").toString();  // Get the username
                }
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
        return username;
    }

    @Override
    public void deleteAuth(String authToken) throws DataAccessException {
        String sql = "DELETE FROM AuthDAO WHERE authToken = ?";  // Delete
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement(sql)) {
                preparedStatement.setString(1, authToken);
                preparedStatement.executeQuery();
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public Map<String, AuthData> getAuthTokens() throws DataAccessException {
        return null;
    }

    public boolean isEmpty(){   // Checks to see if is empty
        String sql = "SELECT COUNT(*) from AuthDAO";  // Counts the number of items in the DAO
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
