package hniknam74.circuitsolver.api.elements;

import hniknam74.circuitsolver.api.graph.ElmSide;

public class ResElm extends CircuitElm
{

//	public double current;
//	public double[] volt = new double[2];
	public double r=-1;
	
	public ResElm(int r)
	{
		super(ElmSide.LEFT_TO_RIGHT);
		this.r = r;
	}
	
	
//	public double calcCurrent() {
//		
//		this.current = (volt[0]-volt[1])/r;
//		return current;
//	}
}
