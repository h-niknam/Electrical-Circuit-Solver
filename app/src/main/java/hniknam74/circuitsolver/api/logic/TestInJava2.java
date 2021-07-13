package ir.h_niknam.circuitsolver.api.logic;


import ir.h_niknam.circuitsolver.api.elements.ResElm;
import ir.h_niknam.circuitsolver.api.elements.VoltElm;
import ir.h_niknam.circuitsolver.api.graph.Edge;
import ir.h_niknam.circuitsolver.api.graph.ElmHV;
import ir.h_niknam.circuitsolver.api.graph.ElmSide;
import ir.h_niknam.circuitsolver.api.graph.Mesh;

public class TestInJava2 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		VoltElm v1 = new VoltElm(20, ElmSide.BOT_TO_TOP);
		ResElm r2 = new ResElm(6);
		VoltElm v3 = new VoltElm(10,ElmSide.TOP_TO_BOT);
		ResElm r5 = new ResElm(4);
		
		Edge e0 = new Edge(0, ElmHV.HORIZENTAL,null,true);
		Edge e1 = new Edge(1,ElmHV.VERTICAL,v1,true);		
		Edge e2 = new Edge(2,ElmHV.HORIZENTAL,r2,true);		
		Edge e3 = new Edge(3,ElmHV.VERTICAL,v3,false);
		//Edge e3 = new Edge(3,ElmHV.VERTICAL,r2,false);
		Edge e4 = new Edge(4,ElmHV.HORIZENTAL,null,true);	
		Edge e5 = new Edge(5,ElmHV.VERTICAL,r5,true);
		Edge e6 = new Edge(6,ElmHV.HORIZENTAL,null,true);
		
		Mesh m1 = new Mesh(1);
		e0.side = ElmSide.RIGHT_TO_LEFT;
		e1.side = ElmSide.BOT_TO_TOP;
		e2.side = ElmSide.LEFT_TO_RIGHT;
		e3.side = ElmSide.TOP_TO_BOT;
		m1.edgs.add(e0);
		m1.edgs.add(e1);
		m1.edgs.add(e2);
		m1.edgs.add(e3);
		
		Mesh m2  = new Mesh(2);
		e6.side = ElmSide.RIGHT_TO_LEFT;
		//e3.side = ElmSide.BOT_TO_TOP;
		e4.side = ElmSide.LEFT_TO_RIGHT;
		e5.side = ElmSide.TOP_TO_BOT;
		m2.edgs.add(e6);
		m2.edgs.add(e3);
		m2.edgs.add(e4);
		m2.edgs.add(e5);
		
		Circuit c = new Circuit();
		c.meshes.add(m1);
		c.meshes.add(m2);
		c.kvl();
		
		
	}

}
