package ast;

import util.context.Position;

import java.util.ArrayList;

public class RootNode extends ASTNode {
    public FnRootNode fn;
    public ArrayList<StructDeclarationNode> structs = new ArrayList<>();

    public RootNode(Position pos, FnRootNode fn) {
        super(pos);
        this.fn = fn;
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
