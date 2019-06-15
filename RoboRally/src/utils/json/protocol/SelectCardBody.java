package utils.json.protocol;

public class SelectCardBody {
    private String card;
    private int register;

    public SelectCardBody(String card, int register) {
        this.card = card;
        this.register = register;
    }

    public String getCard() {
        return card;
    }

    public int getRegister() {
        return register;
    }
}
