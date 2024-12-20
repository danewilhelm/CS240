package client;

import org.junit.jupiter.api.*;
import server.Server;


public class ServerFacadeTests {

    private static Server server;
    private static ServerFacade facade;
    private static int port;
    @BeforeAll
    public static void init() {
        server = new Server();
        port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
    }


    @BeforeEach
    void clear() {
        facade = new ServerFacade("http://localhost:" + port);
        facade.http.clear();
    }

    @AfterAll
    static void stopServer() {
        facade.http.clear();
        server.stop();
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
    public void goodLogin2() {
        facade.register("jeff", "123", "net");
        Assertions.assertTrue(facade.login("jeff", "123"));
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
    public void badCreateGame2() {
        Assertions.assertFalse(facade.createGame("copycat"));
    }



    @Test
    public void goodJoinGame() {
        facade.register("goodRegister", "123", "net");
        facade.createGame("the best game ever");
        Assertions.assertTrue(facade.joinGame("WHITE", 1));
    }

    @Test
    public void goodJoinGame2() {
        facade.register("goodRegister", "123", "net");
        facade.createGame("the best game ever");
        Assertions.assertTrue(facade.joinGame("white", 1));
    }

    @Test
    public void badJoinGame() {
        facade.register("goodRegister", "123", "net");
        facade.createGame("the best game ever");
        Assertions.assertFalse(facade.joinGame("purple", 1));
    }

    @Test
    public void badJoinGame2() {
        facade.register("goodRegister", "123", "net");
        facade.createGame("the best game ever");
        Assertions.assertFalse(facade.joinGame("WHITE", 69));
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
    public void badLogout2() {
        facade.register("14978y1354", "12414", "123");
        Assertions.assertTrue(facade.logout());
        Assertions.assertFalse(facade.logout());
    }

    @Test
    public void goodListGames() {
        facade.register("123135", "12414", "123");
        facade.createGame("BEST GAME EVER");
        Assertions.assertNotNull(facade.listGames());
    }

    @Test
    public void badListGames() {
        Assertions.assertNull(facade.listGames());
    }

    @Test
    public void badListGames2() {
        facade.register("123135", "12414", "123");
        facade.logout();
        Assertions.assertNull(facade.listGames());
    }
}
