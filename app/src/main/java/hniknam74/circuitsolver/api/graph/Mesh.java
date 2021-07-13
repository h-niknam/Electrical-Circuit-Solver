package ir.h_niknam.circuitsolver.api.graph;

import java.util.ArrayList;

public class Mesh {

	public int num;
	public ArrayList<Edge> edgs = new ArrayList<Edge>();


	public Mesh(int num) {
		this.num = num;
	}

	public Mesh(int num,ArrayList<Edge> edgs) {
		this.num = num;
		this.edgs = edgs;
	}
}
