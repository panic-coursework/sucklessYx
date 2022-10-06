package ast;

import util.type.BoolType;
import util.type.IntType;
import util.context.Position;
import util.type.Type;

import java.util.ArrayList;

public class FnRootNode extends ASTNode {
    public ArrayList<StmtNode> statements;
    public Type intType, boolType;

    public FnRootNode(Position pos) {
        super(pos);
        statements = new ArrayList<>();
        intType = new IntType();
        boolType = new BoolType();
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
