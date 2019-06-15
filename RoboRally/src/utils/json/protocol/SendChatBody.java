package utils.json.protocol;

public class SendChatBody {
    private String message;
    private int to;

    public SendChatBody(String message, int to) {
        this.message = message;
        this.to = to;
    }

    public String getMessage() {
        return message;
    }

    public int getTo() {
        return to;
    }
}
