package clientTests;

import chess.ChessGame;
import dataAccess.*;
import exception.ResponseException;
import exceptions.UnauthorizedException;
import model.AuthData;
import model.GameData;
import org.junit.jupiter.api.*;
import ui.GamePlayClient;
import response.GameResponseClass;
import response.ResponseClass;
import server.Server;
import server.ServerFacade;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Collection;
import java.util.Collections;


public class ServerFacadeTests {

    private static Server server;
    private static ServerFacade serverFacade;
    private static AuthDAO authDAO;
    private static GameDAO gameDAO;
    private static UserDAO userDAO;

    @BeforeAll
    public static void init() throws DataAccessException {
        server = new Server();
        var port = server.run(0);
        serverFacade = new ServerFacade("http://localhost:" + port);
        System.out.println("Started test HTTP server on " + port);

        authDAO = new DatabaseAuthDAO();
        authDAO.clear();
        gameDAO = new DatabaseGameDAO();
        gameDAO.clear();
        userDAO = new DatabaseUserDAO();
        userDAO.clear();
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }

    @AfterEach
    void reset() throws DataAccessException {
        authDAO.clear();
        gameDAO.clear();
        userDAO.clear();
    }

    @Test
    public void registerPositive() throws ResponseException, UnauthorizedException, DataAccessException {
        // Call register service and store authtoken
        ResponseClass response = serverFacade.register("username", "password", "email");
        ResponseClass response2 = serverFacade.register("username2", "password2", "email2");
        String authToken = response.getAuthToken();
        String authToken2 = response2.getAuthToken();

        // See if users are correctly registered
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        Assertions.assertEquals(userDAO.getUser("username").username(), "username");
        Assertions.assertEquals(userDAO.getUser("username").email(), "email");
        Assertions.assertTrue(encoder.matches("password", userDAO.getUser("username").password()));
        Assertions.assertEquals(userDAO.getUser("username2").username(), "username2");
        Assertions.assertEquals(userDAO.getUser("username2").email(), "email2");
        Assertions.assertTrue(encoder.matches("password2", userDAO.getUser("username2").password()));

        // Create authdata objects that should be stored
        AuthData theAuthData = new AuthData(authToken, "username");
        AuthData theAuthData2 = new AuthData(authToken2, "username2");

        // See if users are correctly logged in
        Assertions.assertEquals(authDAO.getAuth(authToken), theAuthData);
        Assertions.assertEquals(authDAO.getAuth(authToken2), theAuthData2);
    }

    @Test
    public void registerNegative() throws ResponseException {
        // Call register service that should work
        ResponseClass response = serverFacade.register("username", "password", "email");

        // See if an error comes from registering the same username twice
        Assertions.assertThrows(ResponseException.class, () -> serverFacade.register("username", "password2", "email2"));
    }


    @Test
    public void loginPositive() throws DataAccessException, UnauthorizedException, ResponseException {
        // Register new users
        serverFacade.register("username", "password", "email");
        serverFacade.register("username2", "password2", "email2");

        // Log in
        ResponseClass response = serverFacade.login("username", "password");
        ResponseClass response2 = serverFacade.login("username2", "password2");

        // Get authtokens from responses and create authrokens from them
        String authToken = response.getAuthToken();
        String authToken2 = response2.getAuthToken();
        AuthData theAuthData = new AuthData(authToken, "username");
        AuthData theAuthData2 = new AuthData(authToken2, "username2");

        // See if users are logged in with there authtokens
        Assertions.assertEquals(authDAO.getAuth(authToken), theAuthData);
        Assertions.assertEquals(authDAO.getAuth(authToken2), theAuthData2);
    }

    @Test
    public void loginNegative() throws ResponseException {
        // Logging in without valid username
        Assertions.assertThrows(ResponseException.class, () -> serverFacade.login("username", "password"));

        // Register new user and try logging in with the wrong password
        serverFacade.register("username", "password", "email");
        Assertions.assertThrows(ResponseException.class, () -> serverFacade.login("username", "password2"));

    }

    @Test
    public void createGamePositive() throws DataAccessException, ResponseException {
        // Register user and create the game
        ResponseClass registerResponse = serverFacade.register("username", "password", "email");
        ResponseClass gameResponse = serverFacade.createGame(registerResponse.getAuthToken(), "game1");
        int id = gameResponse.getGameID();
        GameData game = gameDAO.getGame(id);
        ChessGame the_game = game.game();
        Assertions.assertNotNull(the_game);  // See if the game was actually added
        Assertions.assertEquals(the_game, new ChessGame());  // See if a new game was added
    }
    @Test
    public void createGameNegative() {
        // See if creating a game without registering before creates an error
        Assertions.assertThrows(ResponseException.class, () ->  serverFacade.createGame("wrong token", "game1"));
    }

    @Test
    public void listGamesPositive() throws DataAccessException, ResponseException {
        // Register user and create the game
        ResponseClass registerResponse = serverFacade.register("username", "password", "email");
        ResponseClass gameResponse = serverFacade.createGame(registerResponse.getAuthToken(), "game1");
        int id = gameResponse.getGameID();
        GameResponseClass response = serverFacade.listGames(registerResponse.getAuthToken());
        // Create the game and see if it is euqal to newGames
        Collection<GameData> newGames = Collections.singleton(new GameData(id, null, null, "game1", new ChessGame()));
        Assertions.assertTrue(response.Equals(newGames));
    }
    @Test
    public void listGamesNegative()  {
        // Should return an error if the user is not authorized to access the games
        Assertions.assertThrows(ResponseException.class, () ->  serverFacade.listGames("wrong token"));

    }

    @Test
    public void logOutPositive() throws DataAccessException, UnauthorizedException, ResponseException {
        ResponseClass registerResponse = serverFacade.register("username", "password", "email");
        String authToken = registerResponse.getAuthToken();
        serverFacade.logOut(authToken);
        Assertions.assertNull(authDAO.getAuth(authToken));  //Should no longer contain the authToken
    }
    @Test
    public void logOutNegative() throws ResponseException {
        serverFacade.register("username", "password", "email");
        Assertions.assertThrows(UnauthorizedException.class, () -> authDAO.deleteAuth("IncorrectAuthToken"));  // Error should be thrown if same username is created
    }

    @Test
    public void joinGamePositive() throws ResponseException {
        ResponseClass registerResponse = serverFacade.register("username", "password", "email");
        String authToken = registerResponse.getAuthToken();
        ResponseClass gameResponse = serverFacade.createGame(authToken, "game1");
        int id = gameResponse.getGameID();
        serverFacade.joinGame(authToken, "WHITE", id);

        GameResponseClass response = serverFacade.listGames(registerResponse.getAuthToken());
        // Create the game and see if it is equal to newGames
        Collection<GameData> newGames = Collections.singleton(new GameData(id, "username", null, "game1", new ChessGame()));
        Assertions.assertTrue(response.Equals(newGames));
    }

    @Test
    public void joinGameNegative() throws ResponseException {
        ResponseClass registerResponse = serverFacade.register("username", "password", "email");
        String authToken = registerResponse.getAuthToken();
        ResponseClass gameResponse = serverFacade.createGame(authToken, "game1");
        int id = gameResponse.getGameID();

        // Try joining without an auth token or with wrong id
        Assertions.assertThrows(ResponseException.class, () -> serverFacade.joinGame("invalidAth", "WHITE", id));
        Assertions.assertThrows(ResponseException.class, () -> serverFacade.joinGame(authToken, "WHITE", 0));

        // Try joining a game that already is taken
        serverFacade.joinGame(authToken, "WHITE", id);
        Assertions.assertThrows(ResponseException.class, () -> serverFacade.joinGame(authToken, "WHITE", id));
    }

    @Test
    public void observePositive() throws ResponseException {
        ResponseClass registerResponse = serverFacade.register("username", "password", "email");
        String authToken = registerResponse.getAuthToken();
        ResponseClass gameResponse = serverFacade.createGame(authToken, "game1");
        int id = gameResponse.getGameID();
        Assertions.assertDoesNotThrow(() -> serverFacade.joinGame(authToken, null, id));
    }

    @Test
    public void observeNegative() throws ResponseException {
        ResponseClass registerResponse = serverFacade.register("username", "password", "email");
        String authToken = registerResponse.getAuthToken();
        ResponseClass gameResponse = serverFacade.createGame(authToken, "game1");
        int id = gameResponse.getGameID();

        // Try observing without an auth token or with wrong id
        Assertions.assertThrows(ResponseException.class, () -> serverFacade.joinGame(authToken, null, 0));
        Assertions.assertThrows(ResponseException.class, () -> serverFacade.joinGame("wrongToken", null, id));
    }

    @Test
    public void display() throws ResponseException {  // Test the display of the chess game
//        GamePlayClient gamePlay = new GamePlayClient("server", "token");
//        Assertions.assertDoesNotThrow(gamePlay::draw);   // See if no error is thrown
    }
}
