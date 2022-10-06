package ast;

import util.type.Type;
import util.context.Position;

public class BinaryExprNode extends ExprNode {
    public ExprNode lhs, rhs;
    public enum Op {
        ADD, SUB
    }
    public Op op;

    public BinaryExprNode(ExprNode lhs, ExprNode rhs, Op op, Type operandType, Position pos) {
        super(pos);
        this.lhs = lhs;
        this.rhs = rhs;
        this.op = op;
        type = operandType;
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
