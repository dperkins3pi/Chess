import dataAccess.*;
import org.junit.jupiter.api.*;
import passoffTests.obfuscatedTestClasses.TestServerFacade;
import passoffTests.testClasses.TestException;
import passoffTests.testClasses.TestModels;
import server.Server;
import service.*;
import java.util.HashMap;
import java.util.Map;

import java.net.HttpURLConnection;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ServiceTests{

    private static AuthDAO authDAO;
    private static GameDAO gameDAO;
    private static UserDAO userDAO;

    @BeforeEach
    public void setup() throws TestException {
        authDAO = new MemoryAuthDAO();
        gameDAO = new MemoryGameDAO();
        userDAO = new MemoryUserDAO();
    }
    @Test
    public void clearService() throws Exception {
        // Arbitrarily add stuff to each DAO
        authDAO.createAuth("Frank");
        authDAO.createAuth("John");
        authDAO.createAuth("Cena");
        userDAO.createUser("Username", "Password", "Email");
        userDAO.createUser("Username2", "Password2", "Email2");

        // Call remove service
        ClearService service = new ClearService(authDAO, gameDAO, userDAO);
        service.clear();



        // See if things were removed
        Assertions.assertEquals(authDAO.getAuthTokens(), new HashMap<>());
        Assertions.assertEquals(gameDAO.getGames(), new HashMap<>());
        Assertions.assertEquals(userDAO.getUsers(), new HashMap<>());
    }


    //        Assertions.assertThrows(UnauthorizedException.class, () -> service.clear());
}
