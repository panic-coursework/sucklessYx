package util.context;

import util.error.SemanticError;
import util.type.Type;

import java.util.HashMap;

public class GlobalScope extends Scope {
    private final HashMap<String, Type> types = new HashMap<>();

    public GlobalScope(Scope parentScope) {
        super(parentScope);
    }

    public void addType(String name, Type t, Position pos) {
        if (types.containsKey(name))
            throw new SemanticError("multiple definition of " + name, pos);
        types.put(name, t);
    }

    public Type getTypeFromName(String name, Position pos) {
        if (types.containsKey(name)) return types.get(name);
        throw new SemanticError("no such type: " + name, pos);
    }
}
