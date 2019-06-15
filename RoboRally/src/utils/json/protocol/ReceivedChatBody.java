package utils.json.protocol;

public class ReceivedChatBody {
    private String message;
    private int from;
    private boolean isPrivate;

    public ReceivedChatBody(String message, int from, boolean isPrivate) {
        this.message = message;
        this.from = from;
        this.isPrivate = isPrivate;
    }

    public String getMessage() {
        return message;
    }

    public int getFrom() {
        return from;
    }

    public boolean isPrivate() {
        return isPrivate;
    }
}
