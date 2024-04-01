package dataAccess;

import exceptions.AlreadyTakenException;
import exceptions.UnauthorizedException;
import model.UserData;

import java.sql.ResultSet;
import java.sql.SQLException;

public class DatabaseUserDAO implements UserDAO{
    public DatabaseUserDAO() throws DataAccessException {
        configureDatabase();
    }
    @Override
    public void clear() throws DataAccessException {
        String sql = "DELETE FROM userDAO";  // SQL command
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement(sql)) {
                preparedStatement.executeUpdate(); // Run the commnand
            }
        } catch (SQLException ex) {   // If the table was never created
            throw new DataAccessException(String.format("Unable to clear user: %s", ex.getMessage()));
        }
    }

    @Override
    public void createUser(String username, String password, String email) throws DataAccessException, UnauthorizedException, AlreadyTakenException {
        if(isValid(username)) throw new AlreadyTakenException("Invalid authtoken");
        String sql = "INSERT INTO userDAO (username, password, email) values (?, ?, ?)";  // SQL command
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement(sql)) {
                preparedStatement.setString(1, username);   // Get the data
                preparedStatement.setString(2, password);
                preparedStatement.setString(3, email);
                preparedStatement.executeUpdate(); // Perform the statement
            }
        } catch (SQLException ex) {   // If the table was never created
            throw new DataAccessException(String.format("Unable to create user: %s", ex.getMessage()));
        }
    }

    @Override
    public UserData getUser(String username) throws DataAccessException {
        String sql = "SELECT username, password, email FROM userDAO WHERE username = ?";  // Select user
        String password = null;
        String email = null;
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement(sql)) {
                preparedStatement.setString(1, username);
                ResultSet rs = preparedStatement.executeQuery();
                while(rs.next()){
                    password = rs.getObject("password").toString();  // Get the password
                    email = rs.getObject("email").toString();  // Get the email
                }
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
        if(password == null) return null;
        return new UserData(username, password, email);
    }

    private boolean isValid(String username) throws DataAccessException, UnauthorizedException {   // Sees if the authtoken is valid
        UserData user = getUser(username);
        return user != null;
    }

    public boolean isEmpty(){   // Checks to see if is empty
        String sql = "SELECT COUNT(*) from userDAO";  // Counts the number of items in the DAO
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
            CREATE TABLE IF NOT EXISTS  userDAO (
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
