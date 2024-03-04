package dataAccess;

import exceptions.AlreadyTakenException;
import exceptions.BadRequestException;
import exceptions.UnauthorizedException;
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
    public String createAuth(String username) throws DataAccessException, BadRequestException {
        String newToken = UUID.randomUUID().toString();  // Create authToken
        if (username == null) throw new BadRequestException("Invalid username");

        String sql = "INSERT INTO AuthDAO (authToken, username) values (?, ?)";  // SQL command
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
                if(rs.next()) {
                    System.out.println("ya" + rs.getObject("username"));
                    return true;  // Get the username
                }
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
        return false;
    }

    public boolean containsAuth(String authToken){   // Sees if the username already in DAO
        String sql = "SELECT username FROM AuthDAO WHERE authToken = ?";  // SQL command
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement(sql)) {
                preparedStatement.setString(1, authToken);   // Get the data
                ResultSet rs = preparedStatement.executeQuery(); // Perform the statement
                if(rs.next()) {
                    System.out.println("cot");
                    return true;  // Get the username
                }
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
        return false;
    }

    @Override
    public AuthData getAuth(String authToken) throws DataAccessException, UnauthorizedException {
        String username = null;
        String sql = "SELECT username FROM authDAO WHERE authToken = ?";
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
        if (username == null) return null;
        return new AuthData(authToken, username);
    }

    @Override
    public String getUsername(String authToken) throws DataAccessException, UnauthorizedException {
        if(!isValid(authToken)) throw new UnauthorizedException("Invalid authtoken");
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
    public void deleteAuth(String authToken) throws DataAccessException, UnauthorizedException {
        if(!isValid(authToken)) {
            throw new UnauthorizedException("Invalid authtoken");
        }
        String sql = "DELETE FROM authDAO WHERE authToken = ?";  // Delete
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement(sql)) {
                preparedStatement.setString(1, authToken);
                preparedStatement.executeUpdate();
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
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

    private boolean isValid(String authToken) throws DataAccessException, UnauthorizedException {   // Sees if the authtoken is valid
        AuthData authData = getAuth(authToken);
        if (authData == null) return false;
        else return (authData.username() != null);
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
