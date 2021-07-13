package ir.h_niknam.circuitsolver.api.logic;


import ir.h_niknam.circuitsolver.api.elements.ResElm;
import ir.h_niknam.circuitsolver.api.elements.VoltElm;
import ir.h_niknam.circuitsolver.api.graph.Edge;
import ir.h_niknam.circuitsolver.api.graph.ElmHV;
import ir.h_niknam.circuitsolver.api.graph.ElmSide;
import ir.h_niknam.circuitsolver.api.graph.Mesh;

public class TestInJava5 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		
		
		VoltElm v1 = new VoltElm(20, ElmSide.TOP_TO_BOT);
		
		ResElm r1 = new ResElm(5);
		
		
		Edge e0 = new Edge(0, ElmHV.VERTICAL,v1,true);
		Edge e1 = new Edge(1,ElmHV.HORIZENTAL,r1,true);		
		Edge e2 = new Edge(2,ElmHV.VERTICAL,null,true);		
		Edge e3 = new Edge(3,ElmHV.HORIZENTAL,null,true);
		
		Mesh mesh = new Mesh(1);
		mesh.edgs.add(e0);
		mesh.edgs.add(e1);
		mesh.edgs.add(e2);
		mesh.edgs.add(e3);
		
		Circuit c = new Circuit();
		c.meshes.add(mesh);
		
		c.kvl();
		
		
	}

}
