package ast;

import util.context.Position;

import util.type.Type;

public class IntegerLiteralNode extends ExprNode {
    public int value;

    public IntegerLiteralNode(int value, Type intType, Position pos) {
        super(pos);
        this.value = value;
        type = intType;
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
