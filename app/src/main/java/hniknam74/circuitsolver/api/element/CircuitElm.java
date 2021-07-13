package hniknam74.circuitsolver.api.elements;

import hniknam74.circuitsolver.api.graph.ElmSide;

public class CircuitElm {

    public CircuitElm (ElmSide side){
        this.side = side;
    }
	public boolean isReplace = false;
	public boolean isReplaceChecked = false;
	public int num=0;
	public ElmSide side;
}
