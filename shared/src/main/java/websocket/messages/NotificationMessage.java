package websocket.messages;

public class NotificationMessage extends ServerMessage {

    private final String MESSAGE;

    public NotificationMessage(String message) {
        super(ServerMessageType.NOTIFICATION);
        this.MESSAGE = message;
    }

    public String getMessage() {
        return this.MESSAGE;
    }
}
