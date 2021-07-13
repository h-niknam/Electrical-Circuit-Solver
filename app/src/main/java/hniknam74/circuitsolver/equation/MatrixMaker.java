package ir.h_niknam.circuitsolver.equation;

public class MatrixMaker {
    private String[] equations;
    private float[][] coefficientMatrix;
    private float[][] preCoefficientMatrix;
    private float[] constsMatrix;
    int maxI = -1;

    public MatrixMaker(String equations) {
        Parser p = new Parser(equations);
        this.equations = p.equationSeprator();
        coefficientMatrix = new float[this.equations.length][this.equations.length];
        preCoefficientMatrix = new float[this.equations.length][2 * this.equations.length];
        constsMatrix = new float[this.equations.length];
        coefficientMatrixMaker();
        constMatrixMaker();
    }


    private boolean isNumeric(char a) {
        try {
            int b = Integer.parseInt(Character.toString(a));
        } catch (NumberFormatException c) {
            return false;
        }
        return true;
    }


    private float[] coefficientExtractor(String equation) {
        float[] coefficientsI = new float[this.equations.length];
        float[] coefficientsV = new float[this.equations.length];
        for (int i = 0; i < coefficientsV.length; i++) {
            coefficientsI[i] = 0f;
            coefficientsV[i] = 0f;
        }
        for (int i = 0; i < equation.length(); i++) {
            if (equation.charAt(i) == 'I' || equation.charAt(i) == 'V') {
                int index = Integer.parseInt(Character.toString(equation.charAt(i + 1))) - 1;
                if (equation.charAt(i) == 'I') {
                    if (index > maxI) {
                        maxI = index;
                    }
                }
                int counter = i;
                int sign = 1;
                while (equation.charAt(counter) != '*' && counter != 0 && equation.charAt(counter) != '(') {
                    counter--;
                    if (equation.charAt(counter) == '-') {
                        sign = sign * (-1);
                    }
                }
                if (counter == 0 || equation.charAt(counter) == '(') {
                    if (equation.charAt(i) == 'V') {
                        coefficientsV[index] += 1.0f;
                    }
                    if (equation.charAt(i) == 'I') {
                        coefficientsI[index] += 1.0f;
                    }
                } else if (equation.charAt(counter) == '*') {
                    while (!isNumeric(equation.charAt(counter))) {
                        counter--;
                        if (equation.charAt(counter) == '-') {
                            sign = sign * (-1);
                        }
                    }
                    String numberString = "";
                    while (equation.charAt(counter) != '-' && equation.charAt(counter) != '+') {
                        numberString = equation.charAt(counter) + numberString;
                        counter--;
                    }
                    if (equation.charAt(counter) == '-') {
                        sign = sign * (-1);
                    }
                    float num = Float.parseFloat(numberString) * sign;
                    if (equation.charAt(i) == 'V') {
                        coefficientsV[index] += num;
                    }
                    if (equation.charAt(i) == 'I') {
                        coefficientsI[index] += num;
                    }
                }
            }
        }
        float[] coefficients = new float[2 * this.equations.length];
        for (int z = 0; z < coefficientsI.length; z++) {
            coefficients[z] += coefficientsI[z];
        }
        for (int z = coefficientsI.length; z < coefficients.length; z++) {
            coefficients[z] += coefficientsV[z - coefficientsI.length];
        }

        return coefficients;
    }

    private void coefficientMatrixMaker() {
        for (int i = 0; i < equations.length; i++) {
            preCoefficientMatrix[i] = coefficientExtractor(equations[i]);
        }

        for (int i = 0; i < equations.length; i++) {
            for (int z = 0; z < equations.length; z++) {
                coefficientMatrix[i][z] = preCoefficientMatrix[i][z];
            }
        }

        for (int i = 0; i < equations.length; i++) {
            for (int z = maxI + 1; z < equations.length; z++) {
                coefficientMatrix[i][z] = preCoefficientMatrix[i][equations.length + z - maxI - 1];
            }
        }

        for (int i = 0; i < equations.length; i++) {
            for (int j = 0; j < equations.length; j++) {
                System.out.print(coefficientMatrix[i][j] + "  ");
            }
            System.out.println("\n");
        }

    }

    private void constMatrixMaker() {
        for (int i = 0; i < equations.length; i++) {
            constsMatrix[i] = constExtractor(equations[i]);
        }
    }

    private boolean isConst(String num) {
        try {
            Float.parseFloat(num);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    private Float constExtractor(String equation) {
        float oneConst = 0;
        boolean equalSignFlag = false;
        String numString = "";
        for (int i = 0; i < equation.length(); i++) {
            if (equation.charAt(i) == '=') {
                equalSignFlag = true;
                i++;
            }
            if (equalSignFlag) {
                numString = numString + equation.charAt(i);
                if (i == equation.length() - 1) {
                    oneConst = oneConst + Float.parseFloat(numString);
                }
            } else {
                int counter;
                if (equation.charAt(i) == '(') {
                    counter = i;
                    counter++;
                    numString = "";
                    while (equation.charAt(counter) != ')') {
                        numString = numString + equation.charAt(counter);
                        counter++;
                    }
                    if (isConst(numString)) {
                        oneConst = oneConst + (Float.parseFloat(numString) * (-1));

                    }
                    numString = "";
                }
            }
        }
        return oneConst;
    }

    public float[][] getCoefficientMatrix() {
        return coefficientMatrix;
    }

    public float[] getConstsMatrix() {
        return constsMatrix;
    }

}
