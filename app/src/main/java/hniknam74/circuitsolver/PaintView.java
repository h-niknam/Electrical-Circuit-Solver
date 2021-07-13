package ir.h_niknam.circuitsolver;

import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import java.util.ArrayList;

import ir.h_niknam.circuitsolver.api.logic.Circuit;
import ir.h_niknam.circuitsolver.model.LineModel;
import ir.h_niknam.circuitsolver.model.MeshModel;
import ir.h_niknam.circuitsolver.model.PointModel;
import ir.h_niknam.circuitsolver.util.CircuitConvertor;

public class PaintView extends View {
    public int colorDefaultWire;
    public int colorCommonWire;
    public int textColor;
    public int circleColor;

    public final int THICKNESS = 6;

    Context context;
    boolean isTouching = false;
    public ArrayList<LineModel> list = new ArrayList<LineModel>();

    public int lineCounter = 1;
    int meshCounter = 1;

    public PointModel p1 = new PointModel();
    public PointModel p2 = new PointModel();

    public int LW = 100, LH = 100;

    Paint paint = new Paint();
    public PG pg;

    public ArrayList<MeshModel> meshes = new ArrayList<>();

    public int type = 0;

    public void setType(int type) {
        this.type = type;
    }

    public PaintView(Context context) {
        super(context);
        init(context);
    }

    public PaintView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    void init(Context context) {
        this.context = context;
        pg = new PG(context);
        this.LH = (int) convertDpToPixel(90);
        this.LW = (int) convertDpToPixel(110);
        this.colorDefaultWire = Color.parseColor("#333333");
        this.colorCommonWire = Color.parseColor("#40A1A2");
        this.textColor = Color.parseColor("#FAC859");
        this.circleColor = Color.parseColor("#E47D69");


        this.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                float x = motionEvent.getX();
                float y = motionEvent.getY();

                switch (motionEvent.getAction()) {

                    case MotionEvent.ACTION_DOWN: {

                        p1 = new PointModel();
                        p2 = new PointModel();

                        isTouching = true;

                        p1.x = x;
                        p1.y = y;

                        stuckPoints();

                        break;

                    }

                    case MotionEvent.ACTION_MOVE: {

                        break;
                    }

                    case MotionEvent.ACTION_UP: {
                        isTouching = false;


                        p2.x = x;
                        p2.y = y;

                        if (Math.abs(p2.x - p1.x) < convertDpToPixel(30) && Math.abs(p2.y - p1.y) < convertDpToPixel(30)) {
                            //its like click
                            return false;
                        }

                        int side = 0;
                        if (Math.abs(p2.x - p1.x) > Math.abs(p2.y - p1.y)) {//horizental line
                            p2.y = p1.y;
                            if (p2.x > p1.x) {
                                side = LineModel.LTR;
                                p2.x = p1.x + LW;
                            } else {
                                side = LineModel.RTL;
                                p2.x = p1.x - LW;
                            }
                        } else {//vertical line
                            p2.x = p1.x;
                            if (p2.y > p1.y) {


                                side = LineModel.TTB;
                                p2.y = p1.y + LH;
                            } else {
                                side = LineModel.BTT;
                                p2.y = p1.y - LH;
                            }
                        }

                        stuckPoints();

                        if (type != Constatnts.WIRE) {
                            openValuDialog(side);
                        } else {
                            LineModel m = new LineModel();
                            m.id = lineCounter++;
                            m.p1 = p1;
                            m.p2 = p2;
                            m.side = side;
                            m.type = type;
                            m.value = 10;
                            list.add(m);
                            p1 = null;
                            p2 = null;

                            for (int i = 0; i < list.size(); i++) {
                                check4(list.get(i));
                            }
                            makeDependentLinesFalse();

                            invalidate();
                        }


                        break;
                    }
                }

                return true;
            }
        });
    }

    @Override
    protected void onDraw(Canvas canvas) {
        for (int i = 0; i < list.size(); i++) {
            LineModel l = list.get(i);
            paint.setStyle(Paint.Style.STROKE);
            paint.setColor(colorDefaultWire);
            if (!l.dependent) {
                paint.setColor(colorCommonWire);
            }
            paint.setStrokeWidth(THICKNESS);
            PointModel p1 = list.get(i).p1;
            PointModel p2 = list.get(i).p2;
            canvas.drawLine(p1.x, p1.y, p2.x, p2.y, paint);
            paint.setStyle(Paint.Style.FILL);
            paint.setColor(this.circleColor);
            canvas.drawCircle(p1.x, p1.y, 5, paint);
            canvas.drawCircle(p2.x, p2.y, 5, paint);
            pg.draw(canvas, list);

        }
        for (int i = 0; i < meshes.size(); i++) {
            paint.setColor(this.textColor);
            int[] p = meshes.get(i).getCenter();
            paint.setStrokeWidth(20);
            paint.setTextSize(50);
            canvas.drawText(meshes.get(i).id + "", p[0], p[1], paint);
        }

        super.onDraw(canvas);
    }

    public void openValuDialog(final int side) {

        final Dialog di2 = new Dialog(context);
        di2.requestWindowFeature(Window.FEATURE_NO_TITLE);

        di2.setContentView(R.layout.dialog_value);
        di2.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));

        ImageView icon = di2.findViewById(R.id.icon);
        if (type == Constatnts.VOLT) {
            icon.setImageResource(R.mipmap.ic_volt);
        }
        if (type == Constatnts.CURRENT) {
            icon.setImageResource(R.mipmap.ic_current);
        }
        if (type == Constatnts.RES) {
            icon.setImageResource(R.mipmap.ic_r);
        }
        final EditText price = (EditText) di2.findViewById(R.id.value);
        price.setText(10 + "");

        Button ok = di2.findViewById(R.id.btnOk);
        Button cancel = di2.findViewById(R.id.btnCancel);

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LineModel m = new LineModel();
                m.id = lineCounter++;
                m.p1 = p1;
                m.p2 = p2;
                m.side = side;
                m.type = type;
                m.value = Integer.parseInt(price.getText().toString());
                list.add(m);
                p1 = null;
                p2 = null;

                for (int i = 0; i < list.size(); i++) {
                    check4(list.get(i));
                }
                makeDependentLinesFalse();
                di2.cancel();
                invalidate();
            }
        });


        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                di2.cancel();
            }
        });


        di2.setCanceledOnTouchOutside(true);
        di2.setCancelable(true);
        di2.show();
    }

    public void makeDependentLinesFalse() {
        ArrayList<Integer> cc = new ArrayList<>();

        for (int j = 1; j <= lineCounter; j++) {
            int counter = 0;

            for (int i = 0; i < meshes.size(); i++) {
                if (meshes.get(i).hasMeshWithId(j)) {
                    counter++;
                }
            }
            if (counter > 1) {
                cc.add(j);
            }

        }

        for (int i = 0; i < meshes.size(); i++) {
            meshes.get(i).clearFalses();
            for (int j = 0; j < cc.size(); j++) {
                meshes.get(i).makeDependentFalse(cc.get(j));
            }
        }


        for (int i = 0; i < list.size(); i++) {
            list.get(i).dependent = true;
            for (int j = 0; j < cc.size(); j++) {

                if (list.get(i).id == cc.get(j)) {
                    list.get(i).dependent = false;
                }

            }

        }


    }

    public void check4(final LineModel l) {

        if (l.side == 0 || l.side == 2) {

            final ArrayList<LineModel> t = findBelow(l);

            if (t.size() > 0) {
                //find 1s and 3s that match with l and t.get(0)

                ArrayList<LineModel> onethrees = getBySide(1);
                onethrees.addAll(getBySide(3));

                int jIndex = -1;
                for (int j = 0; j < t.size(); j++) {
                    ArrayList<LineModel> onethreesCopy = new ArrayList<>(onethrees);

                    for (int i = 0; i < onethreesCopy.size(); i++) {
                        if (testMatch(onethreesCopy.get(i), l) && testMatch(onethreesCopy.get(i), t.get(j))) {

                        } else {
                            onethreesCopy.remove(i);
                            i--;
                        }
                    }

                    if (onethreesCopy.size() == 2) {
                        onethrees = onethreesCopy;
                        jIndex = j;
                        break;
                    }

                }

                final ArrayList<LineModel> fi = new ArrayList<>();
                fi.add(l);
                if (jIndex != -1) {
                    fi.add(t.get(jIndex));
                }

                fi.addAll(onethrees);

                if (fi.size() == 4) {
                    MeshModel m = new MeshModel(fi, meshCounter);

                    if (!checkRepeatAll(m)) {

                        meshes.add(new MeshModel(fi, meshCounter++));
                    }
                    Log.d("heyhey", "mesh created by :" + fi.get(0).id + " " + fi.get(1).id + " " + fi.get(2).id + " " + fi.get(3).id + " ");

                }

            }

        }
    }

    public boolean checkRepeatAll(MeshModel m) {
        boolean flag = false;
        for (int i = 0; i < meshes.size(); i++) {
            if (checkRepeatSignle(meshes.get(i), m)) {
                flag = true;
                break;
            }
        }
        return flag;
    }

    private boolean checkRepeatSignle(MeshModel m1, MeshModel m2) {
        boolean flag = false;
        for (int i = 0; i < m1.mesh.size(); i++) {
            boolean flag2 = false;

            for (int j = 0; j < m2.mesh.size(); j++) {
                if (m1.mesh.get(i).id == m2.mesh.get(j).id) {

                    flag2 = true;
                    break;

                }
            }

            if (!flag2) {
                flag = true;
                break;
            }
        }
        return !flag;
    }


    public ArrayList<LineModel> findBelow(LineModel l) {
        ArrayList<LineModel> t = new ArrayList<>();
        if (l.side == 0 || l.side == 2) {
            for (int i = 0; i < list.size(); i++) {
                LineModel lt = list.get(i);
                if (lt.side == 0 || lt.side == 2) {
                    if (lt.p1.y > l.p1.y) {
                        t.add(lt);
                    }
                }
            }
        }

        if (l.side == 1 || l.side == 3) {
            for (int i = 0; i < list.size(); i++) {
                LineModel lt = list.get(i);
                if (lt.side == 1 || lt.side == 3) {
                    if (lt.p1.x < l.p1.x) {
                        t.add(lt);
                    }
                }
            }
        }

        return t;
    }

    public ArrayList<LineModel> getBySide(int side) {
        ArrayList<LineModel> t = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).side == side) {
                t.add(list.get(i));
            }

        }
        return t;
    }

    public boolean testMatch(LineModel l1, LineModel l2) {

        if (l1.p1.x == l2.p1.x && l1.p1.y == l2.p1.y) {
            return true;
        }
        if (l1.p2.x == l2.p2.x && l1.p2.y == l2.p2.y) {
            return true;
        }
        if (l1.p2.x == l2.p1.x && l1.p2.y == l2.p1.y) {
            return true;
        }
        if (l1.p1.x == l2.p2.x && l1.p1.y == l2.p2.y) {
            return true;
        }
        return false;
    }

    public void stuckPoints() {
        for (int i = 0; i < list.size(); i++) {
            LineModel l = list.get(i);
            if (Math.abs(l.p1.x - p1.x) < convertDpToPixel(16) && Math.abs(l.p1.y - p1.y) < convertDpToPixel(16)) {
                p1.x = l.p1.x;
                p1.y = l.p1.y;
            }
            if (Math.abs(l.p2.x - p1.x) < convertDpToPixel(16) && Math.abs(l.p2.y - p1.y) < convertDpToPixel(16)) {
                p1.x = l.p2.x;
                p1.y = l.p2.y;
            }
        }

        for (int i = 0; i < list.size(); i++) {
            LineModel l = list.get(i);
            if (Math.abs(l.p1.x - p2.x) < convertDpToPixel(16) && Math.abs(l.p1.y - p2.y) < convertDpToPixel(16)) {
                p2.x = l.p1.x;
                p2.y = l.p1.y;
            }
            if (Math.abs(l.p2.x - p2.x) < convertDpToPixel(16) && Math.abs(l.p2.y - p2.y) < convertDpToPixel(16)) {
                p2.x = l.p2.x;
                p2.y = l.p2.y;
            }
        }
    }

    public void undo() {
        if (list.size() > 0) {
            list.remove(list.size() - 1);

            meshCounter = 1;
            lineCounter--;

            meshes = new ArrayList<>();
            for (int i = 0; i < list.size(); i++) {
                check4(list.get(i));
            }
            makeDependentLinesFalse();

            invalidate();
        }

    }


    public float convertDpToPixel(float dp) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * ((float) metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return px;
    }

    public float convertPixelsToDp(float px) {
        return px / ((float) context.getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }

    public String[] endDesign() {
        CircuitConvertor cc = new CircuitConvertor();
        Circuit c = cc.CreateCircuit(meshes);
        return c.kvl();
    }

}
