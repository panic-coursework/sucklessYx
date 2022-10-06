package mir;

public class Binary extends Stmt {
    public enum Op {
        ADD, SUB, EQ, NEQ
    }

    public Register lhs;
    public Entity op1, op2;
    public Op op;

    public Binary(Register lhs, Entity op1, Entity op2, Op op) {
        super();
        this.lhs = lhs;
        this.op1 = op1;
        this.op2 = op2;
        this.op = op;
        if (this.op1 instanceof Constant) {
            this.op1 = op2;
            this.op2 = op1;
            if (this.op1 instanceof Constant) {
                int i1 = ((Constant) op1).value(), i2 = ((Constant) op2).value(), i;
                if (op == Op.ADD) i = i1 + i2;
                else if (op == Op.SUB) i = i1 - i2;
                else if (op == Op.EQ) i = (i1 == i2) ? 1 : 0;
                else i = (i1 != i2) ? 1 : 0;
                this.op2 = new Constant(i);
                this.op1 = new Constant(0);
            }
        }
        // Now, op1 is either register or zero
    }
}
