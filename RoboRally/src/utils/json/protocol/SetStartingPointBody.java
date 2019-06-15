package utils.json.protocol;

public class SetStartingPointBody {
    private int x;
    private int y;

    public SetStartingPointBody(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
