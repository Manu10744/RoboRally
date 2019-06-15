package utils.json.protocol;

public class HelloServerBody {
    private String group;
    private boolean isAI;
    private String protocol;

    public HelloServerBody(String group, boolean isAI, String protocol) {
        this.group = group;
        this.isAI = isAI;
        this.protocol = protocol;
    }

    public String getGroup() {
        return group;
    }

    public boolean isAI() {
        return isAI;
    }

    public String getProtocol() {
        return protocol;
    }
}
