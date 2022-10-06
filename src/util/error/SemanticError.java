package util.error;

import util.context.Position;

public class SemanticError extends YxError {

    public SemanticError(String msg, Position pos) {
        super("Semantic Error: " + msg, pos);
    }

}
