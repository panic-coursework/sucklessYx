package backend;

import ast.*;
import mir.*;
import util.context.Scope;
import util.context.GlobalScope;

import static mir.Binary.Op.ADD;
import static mir.Binary.Op.SUB;
import static mir.Binary.Op.EQ;
import static mir.Binary.Op.NEQ;

public class IRBuilder extends ASTBaseVisitor {
    private Block currentBlock;
    private final MainFunction mainFunction;
    private Scope currentScope;
    public IRBuilder(MainFunction mainFunction, GlobalScope globalScope) {
        this.mainFunction = mainFunction;
        currentBlock = mainFunction.rootBlock;
        currentScope = globalScope;
    }

    @Override
    public void visit(RootNode it) {
        it.fn.accept(this);
    }

    @Override
    public void visit(FnRootNode it) {
        currentScope = new Scope(currentScope);
        it.statements.forEach(s -> s.accept(this));
        currentScope = currentScope.getParentScope();
    }

    @Override
    public void visit(VariableDeclarationNode it) {
        currentScope.entities.put(it.name, new Register());
    }

    @Override
    public void visit(ReturnStmtNode it) {
        Entity value;
        if (it.value != null) {
            it.value.accept(this);
            value = it.value.val;
        } else value = null;
        currentBlock.pushBack(new Ret(value));
    }

    @Override
    public void visit(BlockStmtNode it) {
        currentScope = new Scope(currentScope);
    }

    @Override
    public void visit(ExprStmtNode it) {
        it.expr.accept(this);
    }

    @Override
    public void visit(IfStmtNode it) {
        it.condition.accept(this);
        Block trueBranch = new Block(), falseBranch = new Block();
        currentBlock.pushBack(new Branch(it.condition.val, trueBranch, falseBranch));

        Block destination = new Block();
        currentBlock = trueBranch;
        it.thenStmt.accept(this);
        currentBlock.pushBack(new Jump(destination));
        currentBlock = falseBranch;
        it.elseStmt.accept(this);
        currentBlock.pushBack(new Jump(destination));
        currentBlock = destination;

        mainFunction.blocks.add(trueBranch);
        mainFunction.blocks.add(falseBranch);
        mainFunction.blocks.add(destination);
    }

    @Override
    public void visit(AssignExprNode it) {
        it.lhs.accept(this);
        if (it.rhs instanceof BinaryExprNode ||
            it.rhs instanceof CompareExprNode) {
            it.rhs.val = it.lhs.val;
            it.rhs.accept(this);
        } else {
            it.rhs.accept(this);
            currentBlock.pushBack(
                    new Binary((Register) it.lhs.val, it.rhs.val, new Constant(0), ADD)
            );
        }
    }

    @Override
    public void visit(BinaryExprNode it) {
        it.lhs.accept(this);
        it.rhs.accept(this);
        Register value;
        if (it.val != null) value = (Register)it.val;
        else {
            value = new Register();
            it.val = value;
        }
        Binary.Op op = it.op == BinaryExprNode.Op.ADD ? ADD : SUB;
        currentBlock.pushBack(new Binary(value, it.lhs.val, it.rhs.val, op));
    }

    @Override
    public void visit(IntegerLiteralNode it) {
        it.val = new Constant(it.value);
    }

    @Override
    public void visit(CompareExprNode it) {
        it.lhs.accept(this);
        it.rhs.accept(this);
        Register value;
        if (it.val != null) value = (Register)it.val;
        else {
            value = new Register();
            it.val = value;
        }
        Binary.Op op = it.op == CompareExprNode.Op.EQ ? EQ : NEQ;
        currentBlock.pushBack(new Binary(value, it.lhs.val, it.rhs.val, op));
    }

    @Override
    public void visit(IdentifierNode it) {
        it.val = currentScope.getEntity(it.name, true);
    }
}
