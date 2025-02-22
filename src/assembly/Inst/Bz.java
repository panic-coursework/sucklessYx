package assembly.Inst;

import assembly.AsmBlock;
import assembly.Operand.Reg;

// This is actually Beqz, that is jump if src == 0: jump to falseBranch
public class Bz extends Inst {
    public Reg src;
    public AsmBlock destination;

    public Bz(Reg src, AsmBlock destination) {
        this.src = src;
        this.destination = destination;
    }

    @Override
    public String toString() {
        return "beqz " + src.toString() + ", " + destination.toString();
    }
}
