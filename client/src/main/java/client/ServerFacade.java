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

    public boolean joinGame(String gameID, String teamColor) {
        return http.joinGame(gameID, teamColor);
    }

    public boolean logout() {
        return http.logout();
    }

    public boolean listGames() {
        return http.listGames();
    }
}
