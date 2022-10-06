package util.error;

import util.context.Position;

abstract public class YxError extends RuntimeException {
    private final Position pos;
    private final String message;

    public YxError(String msg, Position pos) {
        this.pos = pos;
        this.message = msg;
    }

    public String toString() {
        return message + ": " + pos.toString();
    }
}
