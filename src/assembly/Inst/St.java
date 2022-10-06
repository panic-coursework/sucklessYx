package assembly.Inst;

import assembly.Operand.Imm;
import assembly.Operand.PhyReg;

public class St extends Inst {
    public PhyReg rs, addr;
    public Imm offset;

    public St(PhyReg rs, PhyReg addr, Imm offset) {
        this.rs = rs;
        this.addr = addr;
        this.offset = offset;
    }
    @Override
    public String toString() {
        return "sw " + rs + ", " + addr + "(" + offset + ")";
    }
}
