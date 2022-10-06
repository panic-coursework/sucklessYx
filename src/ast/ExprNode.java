package ast;

import mir.Entity;
import util.type.Type;
import util.context.Position;

public abstract class ExprNode extends ASTNode {
    public Type type;
    public Entity val;

    public ExprNode(Position pos) {
        super(pos);
    }

    public boolean isAssignable() {
        return false;
    }
}
