package websocket.messages;

public class ErrorMessage extends ServerMessage {

    private final String MESSAGE;

    public ErrorMessage(String message) {
        super(ServerMessageType.ERROR);
        this.MESSAGE = message;
    }

    public String getErrorMessage() {
        return this.MESSAGE;
    }
}
