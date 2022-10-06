package mir;

import java.util.HashSet;
import java.util.Set;

public class MainFunction {
    public Block rootBlock = new Block();
    public Set<Block> blocks = new HashSet<>();

    public MainFunction() {
        blocks.add(rootBlock);
    }
}
