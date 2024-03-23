package webSocketMessages.serverMessages;

public class NotificationMessage extends ServerMessage{
    String message;
    public NotificationMessage(String message) {
        super(ServerMessage.ServerMessageType.NOTIFICATION);
        this.message = message;
    }
}
