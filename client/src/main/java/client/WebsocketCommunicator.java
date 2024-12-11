package client;

import javax.websocket.*;
import java.net.URI;
import java.net.URL;
import java.util.Scanner;

public class WebsocketCommunicator extends Endpoint {

    private Session session;

    public WebsocketCommunicator(String entireURL) throws Exception {
        URI uri = new URI("ws://localhost:8080/connect");
        // INCOMPLETE: NEEDS TO DYNAMICALLY CONNECT TO THE URL IT IS GIVEN
        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        this.session = container.connectToServer(this, uri);

        this.session.addMessageHandler(new MessageHandler.Whole<String>() {
            public void onMessage(String message) {
                System.out.println(message);
            }
        });
    }

    public void send(String msg) throws Exception {
        this.session.getBasicRemote().sendText(msg);
    }

    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {
        // do nothing lol
    }


}
