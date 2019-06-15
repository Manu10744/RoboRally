package utils.json.protocol;

public class ErrorBody {
    private String error;

    public ErrorBody(String error) {
        this.error = error;
    }

    public String getError() {
        return error;
    }
}
