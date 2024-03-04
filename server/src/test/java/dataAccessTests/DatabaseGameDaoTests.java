package dataAccessTests;

import chess.ChessGame;
import dataAccess.*;
import model.GameData;
import exceptions.BadRequestException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import passoffTests.testClasses.TestException;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class DatabaseGameDaoTests {
    private static DatabaseGameDAO gameDAO;
    @BeforeEach
    public void setup() throws TestException, DataAccessException {
        gameDAO = new DatabaseGameDAO();
        gameDAO.clear();   // Clear it out
    }
    @AfterEach
    public void cleanup() throws TestException, DataAccessException {
        gameDAO.clear();   // Clear it out
    }

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
    public void createGamePositive() throws DataAccessException, BadRequestException {
        // See if creating a game without a name creates an error
        int id = gameDAO.createGame("game1");
        GameData game = gameDAO.getGame(id);
        ChessGame the_game = game.game();
        Assertions.assertNotNull(the_game);  // See if the game was actually added
        Assertions.assertEquals(the_game, new ChessGame());  // See if a new game was added
    }
    @Test
    public void createGameNegative() {
        // See if creating a game without a name creates an error
        Assertions.assertThrows(BadRequestException.class, () -> gameDAO.createGame(null));
    }

    @Test
    public void getGamePositive() throws BadRequestException, DataAccessException {
        int id = gameDAO.createGame("game1");
        GameData game = gameDAO.getGame(id);
        ChessGame the_game = game.game();
        Assertions.assertEquals(the_game, new ChessGame());  // See the correct game is returned
    }
    @Test
    public void getGameNegative() throws BadRequestException, DataAccessException {
        int id = gameDAO.createGame("game1");
        GameData game = gameDAO.getGame(id + 1);  // Get game that doesn't exist
        Assertions.assertNull(game);  // See the correct game is returned
    }

    @Test
    public void listGamesPositive() throws BadRequestException, DataAccessException {
        gameDAO.createGame("game1");
        int id = gameDAO.createGame("game2");
        var games = gameDAO.listGames();
        // See if game2 was added correctly
        Assertions.assertTrue(games.contains(new GameData(id, null, null, "game2", new ChessGame())));
    }
    @Test
    public void listGamesNegative()  {
        var games = gameDAO.listGames();  // Get game that doesn't exist
        Assertions.assertTrue(games.isEmpty());  // See if nothing is there
    }

    @Test
    public void updateGamePositive() throws BadRequestException, DataAccessException {
        gameDAO.createGame("game1");
        int id = gameDAO.createGame("game2");
        gameDAO.createGame("game3");

        GameData new_game = new GameData(id, "white", "black", "new game", new ChessGame());
        gameDAO.updateGame(new_game);
        Assertions.assertEquals(gameDAO.getGame(id), new GameData(id, "white", "black", "new game", new ChessGame()));
    }
    @Test
    public void updateGameNegative() throws BadRequestException, DataAccessException {
        gameDAO.createGame("game1");
        int id = gameDAO.createGame("game2");

        GameData new_game = new GameData(id + 1, "white", "black", "new game", new ChessGame());
        // Throw assertion if you try to update a game that doesn't exist
        Assertions.assertThrows(BadRequestException.class, () -> gameDAO.updateGame(new_game));
    }
}
