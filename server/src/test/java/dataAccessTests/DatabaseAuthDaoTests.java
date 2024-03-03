package dataAccessTests;

import dataAccess.*;
import exceptions.AlreadyTakenException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import passoffTests.testClasses.TestException;
import static org.junit.jupiter.api.Assertions.*;
import service.ClearService;

import java.sql.SQLException;

public class DatabaseAuthDaoTests {
    private static DatabaseAuthDAO authDAO;
    @BeforeEach
    public void setup() throws TestException, DataAccessException {
        authDAO = new DatabaseAuthDAO();
        authDAO.clear();   // Clear it out
    }

    @Test
    public void clearAuthPositive() throws DataAccessException, AlreadyTakenException {
        // Create users
        authDAO.createAuth("username1");
        authDAO.createAuth("username2");
        authDAO.clear();  // Clear it
        assertTrue(authDAO.isEmpty());  // See if no error is thrown
    }

    @Test
    public void createAuthPositive() throws DataAccessException, AlreadyTakenException {
        assertDoesNotThrow(() -> authDAO.createAuth("username1"));  // See if no error is thrown
        assertDoesNotThrow(() -> authDAO.createAuth("username2"));  // See if no error is thrown

        String token = authDAO.createAuth("username3");  // See if no error is thrown
        assertEquals(authDAO.getUsername(token), "username3");   // see if the username was added to the DAO
    }
    @Test
    public void createAuthNegative() throws DataAccessException {
        assertDoesNotThrow(() -> authDAO.createAuth("username1"));  // See if no error is thrown
        assertDoesNotThrow(() -> authDAO.createAuth("username2"));  // See if no error is thrown
        Assertions.assertThrows(AlreadyTakenException.class, () -> authDAO.createAuth("username2"));  // Error should be thrown if same username is created
    }
}
