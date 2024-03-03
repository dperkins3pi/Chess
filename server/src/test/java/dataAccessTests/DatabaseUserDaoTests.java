package dataAccessTests;

import dataAccess.*;
import exceptions.AlreadyTakenException;
import exceptions.UnauthorizedException;
import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import passoffTests.testClasses.TestException;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class DatabaseUserDaoTests {
    private static DatabaseUserDAO userDao;
    @BeforeEach
    public void setup() throws TestException, DataAccessException {
        userDao = new DatabaseUserDAO();
        userDao.clear();   // Clear it out
    }
    @AfterEach
    public void cleanup() throws TestException, DataAccessException {
        userDao.clear();   // Clear it out
    }

    @Test
    public void clearAuthPositive() throws DataAccessException, AlreadyTakenException, UnauthorizedException {
        // Create users
        userDao.createUser("username1", "password1", "email1");
        userDao.createUser("username2", "password2", "email2");
        userDao.clear();  // Clear it
        assertTrue(userDao.isEmpty());  // See if no error is thrown
    }

    @Test
    public void createUserPositive() throws DataAccessException, AlreadyTakenException, UnauthorizedException {
        assertDoesNotThrow(() -> userDao.createUser("username1", "password1", "email1"));  // See if no error is thrown

        userDao.createUser("username2", "password2", "email2");  // See if no error is thrown
        UserData user = userDao.getUser("username2");
        assertEquals(user.username(), "username2");   // see if the username was added to the DAO
        assertEquals(user.password(), "password2");   // see if the username was added to the DAO
        assertEquals(user.email(), "email2");   // see if the username was added to the DAO
    }
    @Test
    public void createUserNegative() throws DataAccessException, UnauthorizedException, AlreadyTakenException {
        userDao.createUser("username1", "password1", "email1");
        userDao.createUser("username2", "password2", "email2");
        Assertions.assertThrows(AlreadyTakenException.class, () -> userDao.createUser("username1", "password3", "email3"));  // Error should be thrown if same username is created
    }

    @Test
    public void getUserPositive() throws DataAccessException, AlreadyTakenException, UnauthorizedException {
        userDao.createUser("username1", "password1", "email1");
        userDao.createUser("username2", "password2", "email2");
        UserData user = userDao.getUser("username1");
        Assertions.assertNotNull(user);
    }
    @Test
    public void getUserNegative() throws DataAccessException, AlreadyTakenException, UnauthorizedException {
        userDao.createUser("username1", "password1", "email1");
        userDao.createUser("username2", "password2", "email2");
        UserData authData = userDao.getUser("Wrong Username");
        Assertions.assertNull(authData.password());
    }
}
