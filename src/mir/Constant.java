package mir;

public class Constant extends Entity {
    private final int value;

    public Constant(int value) {
        super();
        this.value = value;
    }

    public int value() {
        return value;
    }
}
