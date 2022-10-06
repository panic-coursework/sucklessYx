package frontend;

import ast.*;
import parser.YxBaseVisitor;
import parser.YxParser;
import util.context.Position;
import util.type.Type;
import util.context.GlobalScope;
import org.antlr.v4.runtime.ParserRuleContext;
import ast.CompareExprNode.Op;

public class ASTBuilder extends YxBaseVisitor<ASTNode> {

    private final GlobalScope globalScope;

    public ASTBuilder(GlobalScope globalScope) {
        this.globalScope = globalScope;
    }

    Type intType, boolType;

    @Override
    public ASTNode visitProgram(YxParser.ProgramContext ctx) {
        RootNode root = new RootNode(new Position(ctx), (FnRootNode) visit(ctx.mainFn()));
        ctx.classDef().forEach(cd -> root.structs.add((StructDeclarationNode) visit(cd)));
        return root;
    }

    @Override
    public ASTNode visitClassDef(YxParser.ClassDefContext ctx) {
        StructDeclarationNode structDef = new StructDeclarationNode(new Position(ctx), ctx.Identifier().toString());
        ctx.varDef().forEach(vd -> structDef.varDefs.add((VariableDeclarationNode) visit(vd)));
        return structDef;
    }

    @Override
    public ASTNode visitMainFn(YxParser.MainFnContext ctx) {
        FnRootNode root = new FnRootNode(new Position(ctx));
        intType = root.intType;
        boolType = root.boolType;
        globalScope.addType("int", intType, root.pos);
        globalScope.addType("bool", boolType, root.pos);

        if (ctx.suite() != null) {
            for (ParserRuleContext stmt : ctx.suite().statement())
                root.statements.add((StmtNode) visit(stmt));
        }
        return root;
    }

    @Override
    public ASTNode visitVarDef(YxParser.VarDefContext ctx) {
        ExprNode expr = null;
        String typeName;
        if (ctx.type().Int() != null) typeName = ctx.type().Int().toString();
        else typeName = ctx.type().Identifier().toString();
        if (ctx.expression() != null) expr = (ExprNode) visit(ctx.expression());

        return new VariableDeclarationNode(typeName,
                ctx.Identifier().toString(),
                expr, new Position(ctx));
    }

    @Override
    public ASTNode visitSuite(YxParser.SuiteContext ctx) {
        BlockStmtNode node = new BlockStmtNode(new Position(ctx));

        if (!ctx.statement().isEmpty()) {
            for (ParserRuleContext stmt : ctx.statement()) {
                StmtNode tmp = (StmtNode) visit(stmt);
                if (tmp != null) node.statements.add(tmp);
            }
        }
        return node;
    }

    @Override
    public ASTNode visitBlock(YxParser.BlockContext ctx) {
        return visit(ctx.suite());
    }

    @Override
    public ASTNode visitVardefStmt(YxParser.VardefStmtContext ctx) {
        return visit(ctx.varDef());
    }

    @Override
    public ASTNode visitIfStmt(YxParser.IfStmtContext ctx) {
        StmtNode thenStmt = (StmtNode) visit(ctx.trueStmt), elseStmt = null;
        ExprNode condition = (ExprNode) visit(ctx.expression());
        if (ctx.falseStmt != null) elseStmt = (StmtNode) visit(ctx.falseStmt);
        return new IfStmtNode(condition, thenStmt, elseStmt, new Position(ctx));
    }

    @Override
    public ASTNode visitReturnStmt(YxParser.ReturnStmtContext ctx) {
        ExprNode value = null;
        if (ctx.expression() != null) value = (ExprNode) visit(ctx.expression());
        return new ReturnStmtNode(value, new Position(ctx));
    }

    @Override
    public ASTNode visitPureExprStmt(YxParser.PureExprStmtContext ctx) {
        return new ExprStmtNode((ExprNode) visit(ctx.expression()), new Position(ctx));
    }

    @Override
    public ASTNode visitEmptyStmt(YxParser.EmptyStmtContext ctx) {
        return null;
    }

    @Override
    public ASTNode visitAtomExpr(YxParser.AtomExprContext ctx) {
        return visit(ctx.primary());
    }

    @Override
    public ASTNode visitBinaryExpr(YxParser.BinaryExprContext ctx) {
        ExprNode lhs = (ExprNode) visit(ctx.expression(0)),
                rhs = (ExprNode) visit(ctx.expression(1));
        BinaryExprNode.Op biOp = ctx.Plus() != null ? BinaryExprNode.Op.ADD :
                (ctx.Minus() != null ? BinaryExprNode.Op.SUB : null);
        Op cmpOp = ctx.Equal() != null ? Op.EQ :
                (ctx.NotEqual() != null ? Op.NEQ : null);

        return biOp != null ? (new BinaryExprNode(lhs, rhs, biOp, intType, new Position(ctx))) :
                (new CompareExprNode(lhs, rhs, cmpOp, boolType, new Position(ctx)));
    }

    @Override
    public ASTNode visitAssignExpr(YxParser.AssignExprContext ctx) {
        ExprNode lhs = (ExprNode) visit(ctx.expression(0)),
                rhs = (ExprNode) visit(ctx.expression(1));
        return new AssignExprNode(lhs, rhs, new Position(ctx));
    }

    @Override
    public ASTNode visitPrimary(YxParser.PrimaryContext ctx) {
        if (ctx.expression() != null) return visit(ctx.expression());
        else if (ctx.literal() != null) return visit(ctx.literal());
        else return new IdentifierNode(ctx.Identifier().toString(), new Position(ctx.Identifier()));
    }

    @Override
    public ASTNode visitLiteral(YxParser.LiteralContext ctx) {
        return new IntegerLiteralNode(Integer.parseInt(ctx.DecimalInteger().toString()),
                intType, new Position(ctx));
    }

}
