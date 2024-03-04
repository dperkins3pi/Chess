package dataAccessTests;

import dataAccess.*;
import exceptions.AlreadyTakenException;
import exceptions.BadRequestException;
import exceptions.UnauthorizedException;
import model.AuthData;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import passoffTests.testClasses.TestException;
import static org.junit.jupiter.api.Assertions.*;

import java.sql.SQLException;

public class DatabaseAuthDaoTests {
    private static DatabaseAuthDAO authDAO;
    @BeforeEach
    public void setup() throws TestException, DataAccessException {
        authDAO = new DatabaseAuthDAO();
        authDAO.clear();   // Clear it out
    }
    @AfterEach
    public void cleanup() throws TestException, DataAccessException {
        authDAO.clear();   // Clear it out
    }

    @Test
    public void clearAuthPositive() throws DataAccessException, BadRequestException {
        // Create users
        authDAO.createAuth("username1");
        authDAO.createAuth("username2");
        authDAO.clear();  // Clear it
        assertTrue(authDAO.isEmpty());  // See if no error is thrown
    }

    @Test
    public void createAuthPositive() throws DataAccessException, UnauthorizedException, BadRequestException {
        assertDoesNotThrow(() -> authDAO.createAuth("username1"));  // See if no error is thrown
        assertDoesNotThrow(() -> authDAO.createAuth("username2"));  // See if no error is thrown

        String token = authDAO.createAuth("username3");  // See if no error is thrown
        assertEquals(authDAO.getUsername(token), "username3");   // see if the username was added to the DAO
    }
    @Test
    public void createAuthNegative() throws DataAccessException {
        assertDoesNotThrow(() -> authDAO.createAuth("username1"));  // See if no error is thrown
        assertDoesNotThrow(() -> authDAO.createAuth("username2"));  // See if no error is thrown
        Assertions.assertThrows(BadRequestException.class, () -> authDAO.createAuth(null));  // Error should be thrown if same username is created
    }

    @Test
    public void deleteAuthPositive() throws DataAccessException, UnauthorizedException, BadRequestException {
        authDAO.createAuth("username1");
        String token = authDAO.createAuth("username2");
        authDAO.deleteAuth(token);
        Assertions.assertFalse(authDAO.contains("username2"));  //Should no longer contain it
    }
    @Test
    public void deleteAuthNegative() throws DataAccessException, BadRequestException {
        authDAO.createAuth("username1");
        authDAO.createAuth("username2");
        Assertions.assertThrows(UnauthorizedException.class, () -> authDAO.deleteAuth("IncorrectAuthToken"));  // Error should be thrown if same username is created
    }

    @Test
    public void getUsernamePositive() throws DataAccessException, UnauthorizedException, BadRequestException {
        authDAO.createAuth("username1");
        String token = authDAO.createAuth("username2");
        Assertions.assertEquals(authDAO.getUsername(token), "username2");
    }
    @Test
    public void getUsernameNegative() throws DataAccessException, BadRequestException {
        authDAO.createAuth("username1");
        authDAO.createAuth("username2");
        Assertions.assertThrows(UnauthorizedException.class, () -> authDAO.getUsername("IncorrectAuthToken"));  // Error should be thrown if same username is created
    }

    @Test
    public void getAuthPositive() throws DataAccessException, AlreadyTakenException, UnauthorizedException, BadRequestException {
        String token = authDAO.createAuth("username");
        authDAO.getAuth(token);
        Assertions.assertNotNull(authDAO.getAuth(token));
    }
    @Test
    public void getAuthNegative() throws DataAccessException, AlreadyTakenException, UnauthorizedException, BadRequestException {
        authDAO.createAuth("username1");
        authDAO.createAuth("username2");
        AuthData authData = authDAO.getAuth("Wrong Token");
        Assertions.assertNull(authData);
    }
}
