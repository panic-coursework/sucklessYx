package backend;

import mir.Block;
import mir.MainFunction;

public interface Pass {
    void visitBlock(Block b);
    void visitFn(MainFunction f);
}
