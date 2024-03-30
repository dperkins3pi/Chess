package webSocketMessages.serverMessages;

public class NotificationMessage extends ServerMessage{
    public NotificationMessage(String message) {
        super(ServerMessage.ServerMessageType.NOTIFICATION);
        this.message = message;
    }
    public NotificationMessage(ServerMessage serverMessage) {
        super(ServerMessage.ServerMessageType.NOTIFICATION);
        this.message = serverMessage.message;
    }

    public String getMessage() {
        return message;
    }
}
