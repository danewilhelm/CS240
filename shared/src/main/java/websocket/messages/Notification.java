package websocket.messages;

public class Notification extends ServerMessage {

    private final String MESSAGE;

    public Notification(String message) {
        super(ServerMessageType.NOTIFICATION);
        this.MESSAGE = message;
    }

    public String getMessage() {
        return this.MESSAGE;
    }
}
