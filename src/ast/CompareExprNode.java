package ast;

import util.context.Position;
import util.type.Type;

public class CompareExprNode extends ExprNode {
    public ExprNode lhs, rhs;

    public enum Op {
        EQ, NEQ
    }

    public Op op;

    public CompareExprNode(ExprNode lhs, ExprNode rhs, Op op, Type boolType, Position pos) {
        super(pos);
        this.lhs = lhs;
        this.rhs = rhs;
        this.op = op;
        type = boolType;
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
