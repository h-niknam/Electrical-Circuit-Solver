package ir.h_niknam.circuitsolver.api.logic;

import android.util.Log;

import java.util.ArrayList;

import ir.h_niknam.circuitsolver.api.elements.CurrentElm;
import ir.h_niknam.circuitsolver.api.elements.ResElm;
import ir.h_niknam.circuitsolver.api.elements.VoltElm;
import ir.h_niknam.circuitsolver.api.graph.Edge;
import ir.h_niknam.circuitsolver.api.graph.ElmSide;
import ir.h_niknam.circuitsolver.api.graph.Mesh;


public class Circuit
{

    public ArrayList<Mesh> meshes = new ArrayList<Mesh>();

    public int replaceCounter = 1;

    public int vCounter =100;

    public String[] kvl()
    {
        // change CurrentElm with fucking VoltElm and other i2 -i1 = ..

        String[] th = readyForKvl();
        String help = th[0];
//		System.out.println("hlip is: "+help);
        String full = "";

        for (int i = 1; i <= meshes.size(); i++)
        {
            full += singleKvl(i) + "\n";
        }

        full += help;

        Log.d("fuckfuck2", full);
        System.out.println(full);
        String[] out = {full, th[1]};
        return out;
    }

    private boolean findInList(int num, ArrayList<Integer> list)
    {
        for (int t = 0; t < list.size(); t++)
        {
            if (list.get(t) == num)
            {
                return true;
            }
        }
        return false;
    }

    public String[] readyForKvl()
    {
        String helpReadyForKvl = "";
        String tempHelp = "";

        //ArrayList<Integer> visited = new ArrayList<Integer>();

        for (int i = 0; i < meshes.size(); i++)
        {
            Mesh m = meshes.get(i);


            ArrayList<Edge> edges = getMeshByNumber(i + 1).edgs;
            for (int j = 0; j < edges.size(); j++)
            {
                Edge e = edges.get(j);
                if (e.elm != null && e.elm instanceof CurrentElm)
                {

                    //boolean isViseted = findInList(e.uniqueCode, visited);

                    //if (!isViseted) {
                    //visited.add(e.uniqueCode);
                    myLog("readyForKvl: unique code is: " + e.uniqueCode);
                    // replace it with a volt elml

                    CurrentElm c = (CurrentElm) e.elm;
                    VoltElm v = new VoltElm(c.c, c.side);
                    v.isReplaceChecked = true;
                    v.isReplace = true;
                    v.v = -1;
                    v.num = replaceCounter++;

                    int edgeInde = getEdgeIndexByCode(e.uniqueCode, m.edgs);

                    changeCurrentWithVolt(edges.get(edgeInde).uniqueCode, v);

                    //meshes.get(i).edgs.get(edgeInde).elm = v;

                    int m2Number = this.getCommenMeshNumber(m.num,
                            e);
                    if (m2Number == -1)
                    {
                        // i2 = 10
                        //tempHelp += "help: (I" + (i + 1) + ") = " + v.v + " " + "\n";
                        helpReadyForKvl += "help: (I" + (i + 1) + ") = "
                                + c.c + " " + "\n";
                    } else
                    {
                        // i2 - i1 = 10;
                        helpReadyForKvl += "help: (I" + (i + 1) + " - I"
                                + m2Number + ") = " + c.c + " " + "\n";
                    }


                    //}
                }

                if (!e.dependent)
                {
                    changeSide(m.num, e.uniqueCode);
                }
            }
        }
        String[] out = {helpReadyForKvl, tempHelp};
        return out;
    }

    public void changeCurrentWithVolt(int edgeId, VoltElm v)
    {
        for (int i = 0; i < meshes.size(); i++)
        {
            for (int j = 0; j < meshes.get(i).edgs.size(); j++)
            {
                if (meshes.get(i).edgs.get(j).uniqueCode == edgeId)
                {
                    Log.d("fuckyou", "here + " + edgeId);
                    this.meshes.get(i).edgs.get(j).elm = v;
                }
            }
        }
    }


    private int getEdgeIndexByCode(int code, ArrayList<Edge> edges)
    {
        for (int i = 0; i < edges.size(); i++)
        {
            if (edges.get(i).uniqueCode == code)
            {
                return i;
            }
        }
        return -1;
    }

    public String singleKvl(int n)
    {
        String out = "";

        ArrayList<Edge> list = getMeshByNumber(n).edgs;

        for (int i = 0; i < list.size(); i++)
        {
            Edge e = list.get(i);

            if (e.elm == null)
            {

            } else
            {
                out += " ( ";
                if (e.elm instanceof VoltElm)
                {
                    // declare side

                    if (!((VoltElm) e.elm).isReplace)
                    {
                        int sign = checkSameDirection(e.side,
                                ((VoltElm) e.elm).side);
                        Log.d("fuckfuck","checkSameDirection the sign is "+sign);
                        // s+ v
                        if (sign > 0)
                        {
                            out += (((VoltElm) e.elm).v == -1) ? "+V " : "+"
                                    + ((VoltElm) e.elm).v + " ";
                        } else
                        {
                            out += (((VoltElm) e.elm).v == -1) ? "-V " : "-"
                                    + ((VoltElm) e.elm).v + " ";
                        }
                    } else
                    {
                        int sign = checkSameDirection(e.side,
                                ((VoltElm) e.elm).side);
                        Log.d("fuckfuck","checkSameDirection the sign is "+sign);
                        // s+ v
                        if (sign > 0)
                        {

                            out += (((VoltElm) e.elm).v == -1) ? "+V" + ((VoltElm) e.elm).num + " " : "+"
                                    + ((VoltElm) e.elm).v + " ";
                        } else
                        {
                            out += (((VoltElm) e.elm).v == -1) ? "-V" + ((VoltElm) e.elm).num + " " : "-"
                                    + ((VoltElm) e.elm).v + " ";
                        }

                    }


                }
                if (e.elm instanceof ResElm)
                {
                    if (e.dependent)
                    {
                        // + i0 * R
                        out += (((ResElm) e.elm).r == -1) ? "+R " : "+"
                                + ((ResElm) e.elm).r + " ";
                        out += "*I" + n;
                    } else
                    {
                        // + (i0 - i1) * R
                        int commenMeshNumber = getCommenMeshNumber(n, e);
                        out += (((ResElm) e.elm).r == -1) ? "+R " : "+"
                                + ((ResElm) e.elm).r + " ";
                        out += "*(I" + n + " - I" + commenMeshNumber + " )";
                    }

                }
                if (e.elm instanceof CurrentElm)
                {

                    Log.d("fuckyou", "this is it + " + e.uniqueCode);
                    // bad event here.
                    // change current with volt with that size
                    // should know the nodes are changed
                    // every changed item should has the more equation for once
                    return "bad request";
                    //return "";
                }
                out += " ) ";
                if (i < list.size() - 1)
                {
                    out += " + ";
                }
                if (!e.dependent)
                {
                    changeSide(getMeshByNumber(n).num,e.uniqueCode);
                    //int tt = e.uniqueCode;


                }
            }

            // here i have to change direction of edge for new mesh if the edge
            // dependent = false


        }

        out += " = 0";
        return "kvl(" + n + "): " + out;
    }

    public void changeSide(int selfMeshNum, int e)
    {
        for (int i = 0; i < this.meshes.size(); i++)
        {
            if (meshes.get(i).num != selfMeshNum)
            {
                for (int j = 0; j < this.meshes.get(i).edgs.size(); j++)
                {

                    if (this.meshes.get(i).edgs.get(j).uniqueCode == e)
                    {
                        Log.d("fuckyou", "i am in chane side");
                        if (meshes.get(i).edgs.get(j).elm.side == ElmSide.LEFT_TO_RIGHT)
                        {
                            Log.d("fuckyou", "i am in chane side1");
                            meshes.get(i).edgs.get(j).elm.side = ElmSide.RIGHT_TO_LEFT;
                        } else if (meshes.get(i).edgs.get(j).elm.side == ElmSide.RIGHT_TO_LEFT)
                        {
                            Log.d("fuckyou", "i am in chane side2");
                            meshes.get(i).edgs.get(j).elm.side = ElmSide.LEFT_TO_RIGHT;
                        } else if (meshes.get(i).edgs.get(j).elm.side == ElmSide.TOP_TO_BOT)
                        {
                            Log.d("fuckyou", "i am in chane side3");
                            meshes.get(i).edgs.get(j).elm.side = ElmSide.BOT_TO_TOP;
                        } else if (meshes.get(i).edgs.get(j).elm.side == ElmSide.BOT_TO_TOP)
                        {
                            Log.d("fuckyou", "i am in chane side4");
                            meshes.get(i).edgs.get(j).elm.side = ElmSide.TOP_TO_BOT;
                        }
                    }
                }
            }

        }

    }

    public Mesh getMeshByNumber(int num)
    {
        for (int i = 0; i < this.meshes.size(); i++)
        {
            if (this.meshes.get(i).num == num)
            {
                return this.meshes.get(i);
            }
        }

        return null;
    }

    public int getCommenMeshNumber(int currentMeshNumber, Edge currentEdge)
    {
        for (int i = 0; i < meshes.size(); i++)
        {
            if (currentMeshNumber != meshes.get(i).num)
            {
                for (int j = 0; j < meshes.get(i).edgs.size(); j++)
                {
                    Edge e = meshes.get(i).edgs.get(j);
                    if (e.uniqueCode == currentEdge.uniqueCode)
                    {
                        myLog("getCommenMeshNumber: for currentMeshNumber "
                                + currentMeshNumber + " returun "
                                + meshes.get(i).num);

                        return meshes.get(i).num;
                    }

                }
            }

        }
        myLog("getCommenMeshNumber: for currentMeshNumber " + currentMeshNumber
                + " returun -1");

        return -1;
    }


    public int checkSameDirection(ElmSide eSide, ElmSide elmSide)
    {
        Log.d("fuckyou",eSide.name());
        if (eSide == ElmSide.TOP_TO_BOT)
        {
            Log.d("fuckfuck","checkSameDirection1");
            return (elmSide == ElmSide.TOP_TO_BOT) ? 1 : -1;
        } else if (eSide == ElmSide.BOT_TO_TOP)
        {Log.d("fuckfuck","checkSameDirection2");
            return (elmSide == ElmSide.BOT_TO_TOP) ? 1 : -1;
        }
        if (eSide == ElmSide.LEFT_TO_RIGHT)
        {Log.d("fuckfuck","checkSameDirection3");
            return (elmSide == ElmSide.LEFT_TO_RIGHT) ? 1 : -1;
        } else if (eSide == ElmSide.RIGHT_TO_LEFT)
        {Log.d("fuckfuck","checkSameDirection4");
            return (elmSide == ElmSide.RIGHT_TO_LEFT) ? 1 : -1;
        }

        return 0;
    }




    public static final boolean LOG_STATUS = false;

    private void myLog(String msg)
    {
        if (LOG_STATUS)
        {
            System.out.println(msg);
        }
    }

}
