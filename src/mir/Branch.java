package mir;

public class Branch extends TerminalStmt {
    public Entity op;
    public Block trueBranch, falseBranch;

    public Branch(Entity op, Block trueBranch, Block falseBranch) {
        super();
        this.op = op;
        this.trueBranch = trueBranch;
        this.falseBranch = falseBranch;
    }
}
