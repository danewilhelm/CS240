package client;

public class ServerFacade {

    ClientCommunicator http;

    public ServerFacade() {
        http = new ClientCommunicator(this, "localhost:8080");
    }

    public boolean register(String username, String password, String email) {
        return http.register(username, password, email);
    }

    public boolean login(String username, String password) {
        return http.login(username, password);
    }

    public boolean joinGame(String teamColor, int gameID) {
        return http.joinGame(teamColor, gameID);
    }

    public boolean logout() {
        return http.logout();
    }

    public boolean listGames() {
        return http.listGames();
    }

    public boolean createGame(String gameName) {
        return http.createGame(gameName);
    }
}
