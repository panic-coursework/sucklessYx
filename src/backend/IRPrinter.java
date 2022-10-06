package backend;

import mir.*;

import java.io.PrintStream;
import java.util.HashMap;

public class IRPrinter implements Pass {
    private PrintStream out;

    private int blockCnt = 0, regCnt = 0;
    private HashMap<Block, Integer> blockIndex = new HashMap<>();
    private HashMap<Register, Integer> regIndex = new HashMap<>();

    public IRPrinter(PrintStream out) {
        this.out = out;
    }

    public void visitBlock(Block b) {
        out.println(getBlockName(b) + ": ");
        b.getStatements().forEach(this::print);
        b.getSuccessors().forEach(this::visitBlock);
    }

    @Override
    public void visitFn(MainFunction f) {
        visitBlock(f.rootBlock);
    }

    private String getBlockName(Block b) {
        if (blockIndex.containsKey(b)) return "b." + blockIndex.get(b);
        else {
            blockIndex.put(b, blockCnt++);
            return "b." + (blockCnt - 1);
        }
    }
    private String getRegName(Register r) {
        if (regIndex.containsKey(r)) return "%" + regIndex.get(r);
        else {
            regIndex.put(r, regCnt++);
            return "%" + (regCnt - 1);
        }
    }
    private String getEntityString(Entity e) {
        if (e instanceof Register) return getRegName((Register) e);
        else return ((Constant) e).value() + "";
    }
    private String getOpString(Binary.Op op) {
        if (op == Binary.Op.ADD) return "+";
        else if (op == Binary.Op.SUB) return "-";
        else if (op == Binary.Op.EQ) return "==";
        else return "!=";
    }

    private void print(Stmt s) {
        if (s instanceof Binary) {
            Binary b = (Binary) s;
            out.println("\t" + getRegName(b.lhs) + " = " +
                    getEntityString(b.op1) + " " + getOpString(b.op) +
                    " " + getEntityString(b.op2) + ";");
        } else if (s instanceof Jump) {
            Jump j = (Jump) s;
            out.println("\tj " + getBlockName(j.destination) + ";");
        } else if (s instanceof Branch) {
            Branch b = (Branch) s;
            out.println("\tbr " + getEntityString(b.op) + " " +
                    getBlockName(b.trueBranch) + ", " + getBlockName(b.falseBranch) + ";");
        } else if (s instanceof Ret) {
            Ret r = (Ret) s;
            out.println("\tret" + (r.value == null ? "" :
                                    (" " + getEntityString(r.value))) + ";");
        }
    }
}
