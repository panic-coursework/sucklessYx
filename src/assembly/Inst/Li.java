package assembly.Inst;

import assembly.Operand.Imm;
import assembly.Operand.Reg;

public class Li extends Inst {
    public Reg rd;
    public Imm imm;

    public Li(Reg rd, Imm imm) {
        this.rd = rd;
        this.imm = imm;
    }

    @Override
    public String toString() {
        return "li " + rd + ", " + imm;
    }
}
