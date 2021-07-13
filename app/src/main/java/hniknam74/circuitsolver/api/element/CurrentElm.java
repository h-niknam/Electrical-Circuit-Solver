package hniknam74.circuitsolver.api.elements;


import hniknam74.circuitsolver.api.graph.ElmSide;

public class CurrentElm extends CircuitElm
{
	
	
	public int c =-1;

	
	 public CurrentElm(int c, ElmSide side) 
	{
		super(side);
		this.c = c;
		this.side = side;
	}
}
