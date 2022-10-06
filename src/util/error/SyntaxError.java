package util.error;
import util.context.Position;

public class SyntaxError extends YxError {

    public SyntaxError(String msg, Position pos) {
        super("SyntaxError: " + msg, pos);
    }

}
