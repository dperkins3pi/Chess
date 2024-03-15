package clientTests;

import dataAccess.*;
import exception.ResponseException;
import exceptions.UnauthorizedException;
import model.AuthData;
import org.junit.jupiter.api.*;
import response.ResponseClass;
import server.Server;
import server.ServerFacade;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


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

}
