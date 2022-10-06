package ast;

import util.context.Position;

public class IdentifierNode extends ExprNode {
    public String name;

    public IdentifierNode(String name, Position pos) {
        super(pos);
        this.name = name;
        type = null;
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public boolean isAssignable() {
        return true;
    }
}
