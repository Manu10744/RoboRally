package utils.json.protocol;

import modelserver.game.Player;

public class PlayerValuesBody {
    private String name;
    private int figure;

    public PlayerValuesBody(String name, int figure) {
        this.name = name;
        this.figure = figure;
    }

    public String getName() {
        return name;
    }

    public int getFigure() {
        return figure;
    }
}
