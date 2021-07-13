package hniknam74.circuitsolver.api.elements;


import hniknam74.circuitsolver.api.graph.ElmSide;

public class VoltElm extends CircuitElm
{

	
	public int v=-1;

	
	public VoltElm(int v,ElmSide side)
	{
		super(side);
		this.v = v;
		this.side = side;
	}
}
