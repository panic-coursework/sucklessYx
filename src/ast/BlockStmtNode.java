package ast;

import util.context.Position;

import java.util.ArrayList;

public class BlockStmtNode extends StmtNode {
    public ArrayList<StmtNode> statements;

    public BlockStmtNode(Position pos) {
        super(pos);
        this.statements = new ArrayList<>();
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
