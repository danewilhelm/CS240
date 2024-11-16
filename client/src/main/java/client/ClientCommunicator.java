package client;

public class ClientCommunicator {

    String url;
    ServerFacade serverFacade;

    public ClientCommunicator(ServerFacade serverFacade, String port) {
        url = "http://" + port;
        this.serverFacade = serverFacade;
    }

}
