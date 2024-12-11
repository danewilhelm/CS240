package websocket.messages;

public class Error extends ServerMessage {

    private final String MESSAGE;

    public Error(String message) {
        super(ServerMessageType.ERROR);
        this.MESSAGE = message;
    }

    public String getErrorMessage() {
        return this.MESSAGE;
    }
}
