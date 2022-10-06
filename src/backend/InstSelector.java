package backend;

import assembly.*;
import assembly.Inst.*;
import assembly.Operand.*;
import mir.*;
import mir.Ret;

import java.util.HashMap;
import static assembly.Inst.Inst.CalCategory.*;

public class InstSelector implements Pass {
    private HashMap<Block, AsmBlock> blockMap = new HashMap<>();
    private HashMap<Register, VirtualReg> regMap = new HashMap<>();
    private int cnt = 0;

    public AsmFn mainF;
    public InstSelector(AsmFn mainFn) {
        this.mainF = mainFn;
    }

    private AsmBlock getAsmBlock(Block b) {
        if (!blockMap.containsKey(b)) {
            blockMap.put(b, new AsmBlock());
        }
        return blockMap.get(b);
    }
    private VirtualReg getAsmReg(Register r) {
        if (!regMap.containsKey(r)) {
            regMap.put(r, new VirtualReg(cnt++));
        }
        return regMap.get(r);
    }
    private Imm getImm(Constant c) {
        return new Imm(c.value());
    }
    @Override
    public void visitBlock(Block B) {
        AsmBlock b = getAsmBlock(B);
        B.getStatements().forEach(s -> {
            if (s instanceof Jump) {
                Jump j = (Jump) s;
                b.push_back(new Jp(getAsmBlock(j.destination)));
                b.successors.add(getAsmBlock(j.destination));
            } else if (s instanceof Branch) {
                Branch br = (Branch) s;
                VirtualReg src;
                if (br.op instanceof Constant) {
                    src = new VirtualReg(cnt++);
                    b.push_back(new Li(src, getImm((Constant) br.op)));
                }
                else src = getAsmReg((Register) br.op);
                b.push_back(new Bz(src, getAsmBlock(br.falseBranch)));
                b.successors.add(getAsmBlock(br.falseBranch));
                AsmBlock suc = getAsmBlock(br.trueBranch);
                // if (suc.precursor == null) suc.precursor = b;
                // else     // prune-use
                b.push_back(new Jp(suc));
                b.successors.add(suc);
            } else if (s instanceof Ret) {
                Ret r = (Ret) s;
                if (r.value != null) {
                    if (r.value instanceof Register)
                        b.push_back(
                                new Mv(mainF.phyRegs.get(10), getAsmReg((Register) r.value))
                        );
                    else b.push_back(
                            new Li(mainF.phyRegs.get(10), getImm((Constant) r.value))
                    );
                }   // First move the return value to x10, then return
                b.push_back(new assembly.Inst.Ret());
            } else if (s instanceof Binary) {
                Binary bi = (Binary) s;
                Inst.CalCategory op = bi.op == Binary.Op.ADD ? add : sub;
                Reg rd;
                if (bi.op == Binary.Op.EQ || bi.op == Binary.Op.NEQ)
                    rd = new VirtualReg(cnt++);
                else rd = getAsmReg(bi.lhs);
                if (bi.op1 instanceof Constant) {
                    b.push_back(new Li(rd, getImm((Constant) bi.op2)));
                } else if (bi.op2 instanceof Constant) {
                    b.push_back(new IType(rd,
                            getAsmReg((Register)bi.op1),
                            getImm((Constant) bi.op2),
                            op)
                    );
                } else {
                    b.push_back(new RType(rd,
                            getAsmReg((Register) bi.op1),
                            getAsmReg((Register) bi.op2),
                            op));
                }
                if (bi.op == Binary.Op.EQ)
                    b.push_back(new IType(getAsmReg(bi.lhs), rd, new Imm(0), eq));
                else if (bi.op == Binary.Op.NEQ)
                    b.push_back(new IType(getAsmReg(bi.lhs), rd, new Imm(0), ne));
                // Assembly code only has seqz and snez, no seq and sne
            }
        });
    }

    @Override
    public void visitFn(MainFunction f) {
        f.blocks.forEach(B -> mainF.blocks.add(getAsmBlock(B)));
        mainF.rootBlock = getAsmBlock(f.rootBlock);
        f.blocks.forEach(this::visitBlock);
        mainF.stackLength += 4 * cnt;
    }
}
