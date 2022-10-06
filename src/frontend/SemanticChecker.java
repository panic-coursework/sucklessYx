package frontend;

import ast.*;
import util.context.GlobalScope;
import util.context.Scope;
import util.error.SemanticError;
import util.type.BoolType;
import util.type.IntType;
import util.type.StructType;
import util.type.Type;

public class SemanticChecker extends ASTBaseVisitor {
    private final GlobalScope globalScope;
    private Scope currentScope;
    private Type currentStruct = null;

    public SemanticChecker(GlobalScope globalScope) {
        currentScope = this.globalScope = globalScope;
    }

    @Override
    public void visit(RootNode it) {
        it.structs.forEach(sd -> sd.accept(this));
        // we SHOULD check struct definitions first
        it.fn.accept(this);
    }

    @Override
    public void visit(StructDeclarationNode it) {
        currentStruct = globalScope.getTypeFromName(it.name, it.pos);
        it.varDefs.forEach(vd -> vd.accept(this));
        currentStruct = null;
    }

    @Override
    public void visit(FnRootNode it) {
        currentScope = new Scope(currentScope);
        for (StmtNode stmt : it.statements) stmt.accept(this);
    }

    @Override
    public void visit(VariableDeclarationNode it) {
        if (currentStruct != null) {
            StructType struct = (StructType) currentStruct;
            if (struct.members.containsKey(it.name))
                throw new SemanticError("redefinition of member " + it.name, it.pos);
            struct.members.put(it.name, globalScope.getTypeFromName(it.typeName, it.pos));
            if (it.init != null)
                throw new SemanticError("Yx does not support default init of members",
                        it.init.pos);
        }

        if (it.init != null) {
            it.init.accept(this);
            if (!(it.init.type instanceof IntType))
                throw new SemanticError("Semantic Error: type not match. It should be int",
                        it.init.pos);
        }
        currentScope.defineVariable(it.name, globalScope.getTypeFromName(it.typeName, it.pos), it.pos);
    }

    @Override
    public void visit(ReturnStmtNode it) {
        if (it.value != null) {
            it.value.accept(this);
            if (!(it.value.type instanceof IntType))
                throw new SemanticError("Semantic Error: type not match. It should be int",
                        it.value.pos);
        }
    }

    @Override
    public void visit(BlockStmtNode it) {
        if (!it.statements.isEmpty()) {
            currentScope = new Scope(currentScope);
            for (StmtNode stmt : it.statements) stmt.accept(this);
            currentScope = currentScope.getParentScope();
        }
    }

    @Override
    public void visit(ExprStmtNode it) {
        it.expr.accept(this);
    }

    @Override
    public void visit(IfStmtNode it) {
        it.condition.accept(this);
        if (!(it.condition.type instanceof BoolType))
            throw new SemanticError("Semantic Error: type not match. It should be bool",
                    it.condition.pos);
        it.thenStmt.accept(this);
        if (it.elseStmt != null) it.elseStmt.accept(this);
    }

    @Override
    public void visit(AssignExprNode it) {
        it.rhs.accept(this);
        it.lhs.accept(this);
        if (it.rhs.type != it.lhs.type)
            throw new SemanticError("Semantic Error: type not match. ", it.pos);
        if (!it.lhs.isAssignable())
            throw new SemanticError("Semantic Error: not assignable", it.lhs.pos);
        it.type = it.rhs.type;
    }

    @Override
    public void visit(BinaryExprNode it) {
        it.lhs.accept(this);
        it.rhs.accept(this);
        if (!(it.lhs.type instanceof IntType))
            throw new SemanticError("Semantic error: type not match. It should be int",
                    it.lhs.pos);
        if (!(it.rhs.type instanceof IntType))
            throw new SemanticError("Semantic error: type not match. It should be int",
                    it.rhs.pos);
    }

    @Override
    public void visit(CompareExprNode it) {
        it.lhs.accept(this);
        it.rhs.accept(this);
        if (it.rhs.type != it.lhs.type)
            throw new SemanticError("Semantic Error: type not match. ", it.pos);
    }

    @Override
    public void visit(IdentifierNode it) {
        if (!currentScope.containsVariable(it.name, true))
            throw new SemanticError("Semantic Error: variable not defined. ", it.pos);
        it.type = currentScope.getType(it.name, true);
    }
}
