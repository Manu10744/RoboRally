package utils.json.protocol;

public class SetStatusBody {
    private boolean ready;

    public SetStatusBody(boolean ready) {
        this.ready = ready;
    }

    public boolean isReady() {
        return ready;
    }
}
