package ir.h_niknam.circuitsolver.util;

import java.util.ArrayList;

import ir.h_niknam.circuitsolver.Constatnts;
import ir.h_niknam.circuitsolver.api.elements.CircuitElm;
import ir.h_niknam.circuitsolver.api.elements.CurrentElm;
import ir.h_niknam.circuitsolver.api.elements.ResElm;
import ir.h_niknam.circuitsolver.api.elements.VoltElm;
import ir.h_niknam.circuitsolver.api.graph.Edge;
import ir.h_niknam.circuitsolver.api.graph.ElmHV;
import ir.h_niknam.circuitsolver.api.graph.ElmSide;
import ir.h_niknam.circuitsolver.api.graph.Mesh;
import ir.h_niknam.circuitsolver.api.logic.Circuit;
import ir.h_niknam.circuitsolver.model.LineModel;
import ir.h_niknam.circuitsolver.model.MeshModel;

public class CircuitConvertor
{

    public Circuit CreateCircuit(ArrayList<MeshModel> list){

        ArrayList<Mesh> mList = new ArrayList<>();
        for( int i=0; i<list.size(); i++)
        {
            MeshModel mm = list.get(i);
            Mesh m = new Mesh(mm.id);
            for( int j=0; j<mm.mesh.size(); j++)
            {
                LineModel l = mm.mesh.get(j);

                ElmHV s = ElmHV.HORIZENTAL;
                ElmSide es = ElmSide.BOT_TO_TOP;


                if(l.side == 0){
                    s = ElmHV.HORIZENTAL;
                    es = ElmSide.LEFT_TO_RIGHT;
                }
                if(l.side == 1){
                    s = ElmHV.VERTICAL;
                    es = ElmSide.TOP_TO_BOT;
                }
                if(l.side == 2){
                    s = ElmHV.HORIZENTAL;
                    es = ElmSide.RIGHT_TO_LEFT;
                }
                if(l.side == 3){
                    s = ElmHV.VERTICAL;
                    es = ElmSide.BOT_TO_TOP;
                }

                CircuitElm elm = null;
                if(l.type == Constatnts.CURRENT)
                {
                    elm = new CurrentElm(l.value,es);
                }
                if(l.type == Constatnts.RES)
                {
                    elm = new ResElm(l.value);
                }
                if(l.type == Constatnts.VOLT)
                {
                    elm = new VoltElm(l.value,es);
                }

                Edge e = new Edge(l.id,s,elm,l.dependent);
                e.side = es;
                m.edgs.add(e);
            }

            mList.add(m);
        }

        Circuit c = new Circuit();
        c.meshes = mList;
        return c;
    }
}
