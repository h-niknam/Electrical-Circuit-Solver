package ir.h_niknam.circuitsolver.equation;

import java.util.ArrayList;

//this class give equations as constructor's input
public class Parser {
    private String equations;

    public Parser(String equations) {
        this.equations = equations;
    }


    public String[] equationSeprator() {
        ArrayList<String> seprated = new ArrayList<>();
        int equationCounter = -1;
        boolean addFlag = false;

        for (int i = 0; i < equations.length(); i++) {
            if (equations.charAt(i) == '\n' || equations.charAt(i) == 'k' || equations.charAt(i) == 'h') {
                addFlag = false;
            }
            if (addFlag == true) {
                seprated.add(equationCounter, seprated.get(equationCounter) + equations.charAt(i));
                seprated.remove(equationCounter + 1);
            }
            if (equations.charAt(i) == ':') {

                addFlag = true;
                equationCounter++;
                seprated.add("");

            }
        }


        return seprated.toArray(new String[seprated.size()]);
    }


}
