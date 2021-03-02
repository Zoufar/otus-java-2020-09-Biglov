package hw18_jdbc.core.model;

public class Account {
    @Id
    private final String no;
    private final String type;
    private final float rest;

    public Account (String no, String type, float rest) {
        this.no = no;
        this.type = type;
        this.rest = rest;
    }

    public String getNo() {
        return no;
    }

    public String getType() {
        return type;
    }

    public float getRest() { return rest;}

    @Override
    public String toString() {
        return "Accout {" +
                "no=" + no +
                ", type = '" + type + '\'' +
                ", rest = " + rest +
                '}';
    }
}
