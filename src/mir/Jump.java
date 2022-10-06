package mir;

public class Jump extends TerminalStmt {
    public Block destination;
    public Jump(Block destination) {
        super();
        this.destination = destination;
    }
}
