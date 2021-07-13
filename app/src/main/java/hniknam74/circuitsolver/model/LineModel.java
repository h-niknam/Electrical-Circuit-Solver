package ir.h_niknam.circuitsolver.model;

import ir.h_niknam.circuitsolver.Constatnts;

public class LineModel {
    public static final int RTL = 2;//right to left
    public static final int LTR = 0;//left to right
    public static final int BTT = 3;//bottom to top
    public static final int TTB = 1; //top to bottom

    public PointModel p1, p2;
    public int side, id;
    public boolean dependent = true;
    public int type = Constatnts.WIRE;
    public int value;
}
