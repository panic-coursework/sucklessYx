package ast;

import util.context.Position;

public class VariableDeclarationNode extends StmtNode {
    public String name, typeName;
    public ExprNode init;

    public VariableDeclarationNode(String typeName, String name, ExprNode init, Position pos) {
        super(pos);
        this.name = name;
        this.typeName = typeName;
        this.init = init;
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
