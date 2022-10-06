package assembly.Operand;

public class VirtualReg extends Reg {
    public int index;
    public VirtualReg(int index) {
        this.index = index;
    }

    @Override
    public String toString() {
        return "%" + index;
    }
}
