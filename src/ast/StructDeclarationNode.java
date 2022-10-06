package ast;

import util.context.Position;

import java.util.ArrayList;

public class StructDeclarationNode extends ASTNode {
    public ArrayList<VariableDeclarationNode> varDefs = new ArrayList<>();
    public String name;

    public StructDeclarationNode(Position pos, String name) {
        super(pos);
        this.name = name;
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
