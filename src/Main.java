import assembly.AsmFn;
import ast.RootNode;
import backend.*;
import frontend.ASTBuilder;
import frontend.SemanticChecker;
import frontend.SymbolCollector;
import mir.MainFunction;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import parser.YxLexer;
import parser.YxParser;
import util.context.GlobalScope;
import util.YxErrorListener;
import util.error.YxError;

import java.io.FileInputStream;
import java.io.InputStream;

public class Main {
    public static void main(String[] args) throws Exception {

        String name = "test.yx";
        InputStream input = new FileInputStream(name);

        try {
            RootNode ASTRoot;
            GlobalScope globalScope = new GlobalScope(null);

            YxLexer lexer = new YxLexer(CharStreams.fromStream(input));
            lexer.removeErrorListeners();
            lexer.addErrorListener(new YxErrorListener());
            YxParser parser = new YxParser(new CommonTokenStream(lexer));
            parser.removeErrorListeners();
            parser.addErrorListener(new YxErrorListener());
            ParseTree parseTreeRoot = parser.program();

            ASTBuilder astBuilder = new ASTBuilder(globalScope);
            ASTRoot = (RootNode) astBuilder.visit(parseTreeRoot);
            new SymbolCollector(globalScope).visit(ASTRoot);
            new SemanticChecker(globalScope).visit(ASTRoot);

            MainFunction f = new MainFunction();
            new IRBuilder(f, globalScope).visit(ASTRoot);
            new IRPrinter(System.out).visitFn(f);

            AsmFn asmFn = new AsmFn();
            new InstSelector(asmFn).visitFn(f);
            new RegAlloc(asmFn).work();
            new AsmPrinter(asmFn, System.out).print();
        } catch (YxError error) {
            System.err.println(error.toString());
            throw new RuntimeException();
        }
    }
}
