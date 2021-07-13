package ir.h_niknam.circuitsolver.api.logic;


import ir.h_niknam.circuitsolver.api.elements.CurrentElm;
import ir.h_niknam.circuitsolver.api.elements.ResElm;
import ir.h_niknam.circuitsolver.api.graph.Edge;
import ir.h_niknam.circuitsolver.api.graph.ElmHV;
import ir.h_niknam.circuitsolver.api.graph.ElmSide;
import ir.h_niknam.circuitsolver.api.graph.Mesh;

public class TestInJava3 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		//VoltElm v0 = new VoltElm(20, ElmSide.BOT_TO_TOP);
		CurrentElm c0 = new CurrentElm(20, ElmSide.BOT_TO_TOP);
		ResElm r0 = new ResElm(10);
		ResElm r1 = new ResElm(8);
		ResElm r2 = new ResElm(6);
		ResElm r3 = new ResElm(4);
		ResElm r4 = new ResElm(2);
		
		//Edge e0 = new Edge(0, ElmHV.VERTICAL, v0, true);
		Edge e0 = new Edge(0, ElmHV.VERTICAL, c0, true);
		Edge e1 = new Edge(1, ElmHV.HORIZENTAL, r0, false);
		Edge e2 = new Edge(2, ElmHV.VERTICAL, r1, false);
		Edge e3 = new Edge(3, ElmHV.VERTICAL, r2, true);
		Edge e4 = new Edge(4, ElmHV.HORIZENTAL, r3, false);
		Edge e5 = new Edge(5, ElmHV.HORIZENTAL, r4, true);
		
		Mesh m1 = new Mesh(1);
		m1.edgs.add(e0);
		m1.edgs.add(e2);
		m1.edgs.add(e1);
		
		Mesh m2 = new Mesh(2);
		m2.edgs.add(e2);
		m2.edgs.add(e3);
		m2.edgs.add(e4);
		
		Mesh m3 = new Mesh(3);
		m3.edgs.add(e1);
		m3.edgs.add(e4);
		m3.edgs.add(e5);
		
		Circuit c = new Circuit();
		c.meshes.add(m1);
		c.meshes.add(m2);
		c.meshes.add(m3);
		c.kvl();
		
		
	}

}
