package assembly.Inst;

import assembly.Operand.Reg;
import util.context.Position;
import util.error.InternalError;

public class RType extends Inst {
    public Reg rd, rs1, rs2;
    public CalCategory op;

    public RType(Reg rd, Reg rs1, Reg rs2, CalCategory op) {
        this.rd = rd;
        this.rs1 = rs1;
        this.rs2 = rs2;
        this.op = op;
    }

    @Override
    public String toString() {
        if (op.ordinal() < 2) return op + " " + rd + ", " + rs1 + ", " + rs2;
        else throw new InternalError("not correct op for RType", new Position(0, 0));
    }
}
