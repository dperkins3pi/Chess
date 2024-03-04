package dataAccessTests;

import dataAccess.DataAccessException;
import dataAccess.DatabaseAuthDAO;
import dataAccess.DatabaseGameDAO;
import exceptions.AlreadyTakenException;
import exceptions.BadRequestException;
import exceptions.UnauthorizedException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import passoffTests.testClasses.TestException;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class DatabaseGameDaoTests {
    private static DatabaseGameDAO gameDAO;
    @BeforeEach
    public void setup() throws TestException, DataAccessException {
        gameDAO = new DatabaseGameDAO();
        gameDAO.clear();   // Clear it out
    }
//    @AfterEach
//    public void cleanup() throws TestException, DataAccessException {
//        gameDAO.clear();   // Clear it out
//    }

    @Test
    public void clearGamePositive() throws DataAccessException, BadRequestException {
        // Create games
        gameDAO.createGame("Game1");
        gameDAO.createGame("Game2");
        gameDAO.createGame("Game3");
        Assertions.assertFalse(gameDAO.isEmpty());  // Check to see if it isn't empty
        gameDAO.clear();   //Add more, Create Stuff, e.t.c.
        Assertions.assertTrue(gameDAO.isEmpty());  // See if it is empty
    }

    @Test
    public void createGamePositive() throws DataAccessException {
        // See if creating a game without a name creates an error
        Assertions.assertThrows(BadRequestException.class, () -> gameDAO.createGame(null));
    }
    @Test
    public void createGameNegative() throws DataAccessException {
        // See if creating a game without a name creates an error
        Assertions.assertThrows(BadRequestException.class, () -> gameDAO.createGame(null));
    }
}
