package obj;

public class ReceiverObj {
    static int countAgents = 0;
    private String name;
    private String address;
    private String path;

    public ReceiverObj() {
        countAgents++;
    }


    public void setName(String name) {
        this.name = name;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public static int getCountAgents() {
        return countAgents;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getPath() {
        return path;
    }
}
