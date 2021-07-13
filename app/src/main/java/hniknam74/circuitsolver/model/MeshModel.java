package ir.h_niknam.circuitsolver.model;

import java.util.ArrayList;

public class MeshModel {
    public ArrayList<LineModel> mesh = new ArrayList<>();
    public int id;

    public MeshModel(ArrayList<LineModel> mesh, int id) {
        this.mesh = mesh;
        this.id = id;
    }

    public int[] getCenter() {
        int cx = 0, cy = 0;
        for (int i = 0; i < mesh.size(); i++) {
            if (mesh.get(i).side == 0 || mesh.get(i).side == 2) {
                cx = (int) ((float) (mesh.get(i).p1.x + mesh.get(i).p2.x) / 2);

            }
            if (mesh.get(i).side == 1 || mesh.get(i).side == 3) {
                cy = (int) ((float) (mesh.get(i).p1.y + mesh.get(i).p2.y) / 2);

            }
        }
        int[] t = {cx, cy};
        return t;
    }

    public boolean hasMeshWithId(int id) {
        for (int i = 0; i < mesh.size(); i++) {
            if (mesh.get(i).id == id)
                return true;
        }
        return false;
    }

    public void clearFalses() {
        for (int i = 0; i < mesh.size(); i++) {
            mesh.get(i).dependent = true;
        }
    }

    public void makeDependentFalse(int id) {
        for (int i = 0; i < mesh.size(); i++) {
            if (mesh.get(i).id == id)
                mesh.get(i).dependent = false;

        }
    }
}
