package util.error;

import util.context.Position;

public class InternalError extends YxError {

    public InternalError(String msg, Position pos) {
        super("Internal Error: " + msg, pos);
    }

}
