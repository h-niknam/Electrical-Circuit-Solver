package ir.h_niknam.circuitsolver.equation;

import java.util.ArrayList;

public class CircuitSolver {
    private String equations;
    private float answers[];
    private ArrayList<Float> arrangedAnswers=new ArrayList<Float>();

    public CircuitSolver(String equations) {
        this.equations = equations;
        MatrixMaker matrixMaker = new MatrixMaker(equations);

        LinearEquationsSolver linearEquationsSolver = new LinearEquationsSolver(matrixMaker.getCoefficientMatrix(), matrixMaker.getConstsMatrix());
        answers = linearEquationsSolver.getAnswers();
    }

    public float[] getAnswers() {
        return answers;
    }

}
