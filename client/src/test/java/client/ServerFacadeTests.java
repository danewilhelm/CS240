package client;

import org.junit.jupiter.api.*;
import server.Server;


public class ServerFacadeTests {

    private static Server server;
    ServerFacade facade = new ServerFacade();

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }

    @AfterEach
    void clear() {
        facade.http.clear();
        facade = new ServerFacade();
    }


    @Test
    public void goodRegister() {
        Assertions.assertTrue(facade.register("goodRegister", "123", "net"));
    }

    @Test
    public void badRegister() {
        facade.register("goodRegister", "123", "net");
        Assertions.assertFalse(facade.register("goodRegister", "123", "net"));
    }

    @Test
    public void goodLogin() {
        facade.register("goodRegister", "123", "net");
        Assertions.assertTrue(facade.login("goodRegister", "123"));
    }

    @Test
    public void badLogin() {
        Assertions.assertFalse(facade.login("jeff", "456"));
    }

    @Test
    public void goodCreateGame() {
        facade.register("goodRegister", "123", "net");
        Assertions.assertTrue(facade.createGame("the bestie game"));
    }

    @Test
    public void badCreateGame() {
        facade.register("goodRegister", "123", "net");
        Assertions.assertTrue(facade.createGame("copycat"));
        Assertions.assertTrue(facade.createGame("copycat"));
    }

    @Test
    public void goodJoinGame() {
        facade.register("goodRegister", "123", "net");
        facade.createGame("the best game ever");
        Assertions.assertTrue(facade.joinGame("WHITE", 1));
    }

    @Test
    public void badJoinGame() {
        Assertions.assertFalse(facade.joinGame("purple", 69));
    }


    @Test
    public void goodLogout() {
        facade.register("14978y1354", "12414", "123");
        Assertions.assertTrue(facade.logout());
    }

    @Test
    public void badLogout() {
        Assertions.assertFalse(facade.logout());
    }

    @Test
    public void goodListGames() {
        facade.register("123135", "12414", "123");
        Assertions.assertTrue(facade.listGames());
    }

    @Test
    public void badListGames() {
        Assertions.assertFalse(facade.listGames());
    }






}
