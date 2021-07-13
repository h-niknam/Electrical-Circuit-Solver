package ir.h_niknam.circuitsolver.api.graph;


import ir.h_niknam.circuitsolver.api.elements.CircuitElm;

public class Edge {

	public int uniqueCode;
	
	//it will change depend on position to mesh
	public ElmSide side;
	
	//its always const
	public ElmHV hv;
	
	public CircuitElm elm;
	
	
	public Node leftNode;
	public Node righNodet;
	
	
	public int leftNum;
	public int rightNum;
	
	
	public boolean dependent = true;


	public Edge(int uniqueCode, ElmHV hv, CircuitElm elm, boolean dependent) {
		super();
		this.uniqueCode = uniqueCode;
		this.hv = hv;
		this.elm = elm;
		this.dependent = dependent;
	}
	


	//public int commenNum;

	
}
