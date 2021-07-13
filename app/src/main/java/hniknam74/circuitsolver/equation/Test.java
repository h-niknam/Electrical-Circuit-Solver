package ir.h_niknam.circuitsolver.equation;

public class Test {
    public static void main(String[] args) {
        CircuitSolver solver=new CircuitSolver("kvl(1):  ( -V1  )  +  ( +10.0 *I1 )  = 0\n" +
                "help: (I1) = -1"
                );
        System.out.println(solver.getAnswers()[1]);
    }
}
