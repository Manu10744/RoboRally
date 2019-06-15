package utils.json.protocol;

public class HelloClientBody {
    String protocol;

    public HelloClientBody(String protocol) {
        this.protocol = protocol;
    }

    public String getProtocol() {
        return protocol;
    }
}
