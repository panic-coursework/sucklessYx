package mir;

import util.error.InternalError;
import util.context.Position;

import java.util.ArrayList;
import java.util.LinkedList;

public class Block {
    private final LinkedList<Stmt> statements = new LinkedList<>();
    private TerminalStmt tailStmt = null;

    public Block() {
    }

    public void pushBack(Stmt stmt) {
        statements.add(stmt);
        if (stmt instanceof TerminalStmt) {
            if (tailStmt != null) {
                throw new InternalError("multiple tails of a block", new Position(0, 0));
            }
            tailStmt = (TerminalStmt) stmt;
        }
    }

    public ArrayList<Stmt> getStatements() {
        return new ArrayList<>(statements);
    }

    public ArrayList<Block> getSuccessors() {
        ArrayList<Block> ret = new ArrayList<>();
        if (tailStmt instanceof Branch) {
            ret.add(((Branch) tailStmt).trueBranch);
            ret.add(((Branch) tailStmt).falseBranch);
        } else if (tailStmt instanceof Jump) {
            ret.add(((Jump) tailStmt).destination);
        }
        return ret;
    }
}
