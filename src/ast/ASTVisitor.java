package ast;

public interface ASTVisitor {
    void visit(RootNode it);
    void visit(StructDeclarationNode it);
    void visit(FnRootNode it);

    void visit(VariableDeclarationNode it);
    void visit(ReturnStmtNode it);
    void visit(BlockStmtNode it);
    void visit(ExprStmtNode it);
    void visit(IfStmtNode it);

    void visit(AssignExprNode it);
    void visit(BinaryExprNode it);
    void visit(IntegerLiteralNode it);
    void visit(CompareExprNode it);
    void visit(IdentifierNode it);
}
