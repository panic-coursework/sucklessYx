package frontend;

import ast.ASTBaseVisitor;
import ast.RootNode;
import ast.StructDeclarationNode;
import util.context.GlobalScope;
import util.type.StructType;
import util.type.Type;

public class SymbolCollector extends ASTBaseVisitor {
    private final GlobalScope globalScope;

    public SymbolCollector(GlobalScope globalScope) {
        this.globalScope = globalScope;
    }

    @Override
    public void visit(RootNode it) {
        it.structs.forEach(sd -> sd.accept(this));
    }

    @Override
    public void visit(StructDeclarationNode it) {
        Type struct = new StructType();
        it.varDefs.forEach(vd -> vd.accept(this));
        globalScope.addType(it.name, struct, it.pos);
    }
}
