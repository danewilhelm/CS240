package client;

import chess.ChessGame;
import com.google.gson.Gson;
import ui.ChessBoardUI;
import ui.ClientUI;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.Scanner;

public class WebsocketCommunicator extends Endpoint {

    private Session session;


    public WebsocketCommunicator(String entireURL, String teamColor) throws Exception {
        System.out.println(entireURL);
        URI uri = new URI("ws://" + entireURL + "/ws");
        // "ws://localhost:8080/connect"
        // INCOMPLETE: NEEDS TO DYNAMICALLY CONNECT TO THE URL IT IS GIVEN
        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        this.session = container.connectToServer(this, uri);

        this.session.addMessageHandler(new MessageHandler.Whole<String>() {
            public void onMessage(String message) {
                Gson serializer = new Gson();
                ServerMessage serverMessage = serializer.fromJson(message, ServerMessage.class);

                if (serverMessage.getServerMessageType() == ServerMessage.ServerMessageType.NOTIFICATION) {
                NotificationMessage notificationMessage = serializer.fromJson(message, NotificationMessage.class);
                    System.out.println("\nℹ️: "+  notificationMessage.getMessage());
                } else if (serverMessage.getServerMessageType() == ServerMessage.ServerMessageType.ERROR) {
                    NotificationMessage notificationMessage = serializer.fromJson(message, NotificationMessage.class);
                    System.out.println("\n⚠️: "+  notificationMessage.getMessage());
                } else if (serverMessage.getServerMessageType() == ServerMessage.ServerMessageType.LOAD_GAME) {
                    try {
                        LoadGameMessage loadGameMessage = serializer.fromJson(message, LoadGameMessage.class);
                        ChessBoardUI boardPrinter = new ChessBoardUI(
                                loadGameMessage.getGame().game(), teamColor == null ? "WHITE" : teamColor, null);
                        boardPrinter.drawChessBoardUI();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    public void send(String msg) {
        try {
            this.session.getBasicRemote().sendText(msg);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("IOException was thrown when sending over websocket");
        }
    }

    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {
        // do nothing lol
    }


}
