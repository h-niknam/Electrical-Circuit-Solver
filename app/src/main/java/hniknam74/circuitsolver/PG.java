package ir.h_niknam.circuitsolver;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.DisplayMetrics;

import java.util.ArrayList;

import ir.h_niknam.circuitsolver.model.LineModel;
import ir.h_niknam.circuitsolver.model.PointModel;

public class PG
{
    Context context;

    public PG(Context context)
    {
        this.context = context;
    }

    public void draw(Canvas canvas, ArrayList<LineModel> list)
    {
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);

        for (int i = 0; i < list.size(); i++)
        {
            LineModel l = list.get(i);
            if(l.type != Constatnts.WIRE)
            {

                int rotation = 0;
                if (l.side == 0)
                {
                    rotation = 90;
                }
                if (l.side == 2)
                {
                    rotation = -90;
                }
                if (l.side == 1)
                {
                    rotation = 180;
                }

                PointModel p1 = list.get(i).p1;
                PointModel p2 = list.get(i).p2;
                int cx = 0, cy = 0;

                Bitmap b = null;
                if (l.type == Constatnts.RES)
                    b = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_r_color);
                if (l.type == Constatnts.VOLT)
                    b = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_volt_color);
                if (l.type == Constatnts.CURRENT)
                    b = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_current_color);
                Bitmap b2 = RotateBitmap(b, rotation);
                b2 = Bitmap.createScaledBitmap(b2, (int) convertDpToPixel(52), (int) convertDpToPixel(52), false);

                if (l.side == 0 || l.side == 2)
                {
                    cx = (int) ((p1.x + p2.x) / 2) - (b2.getWidth() / 2);
                    cy = (int) (p1.y - (b2.getHeight() / 2));

                    paint.setTextSize(20);
                    paint.setColor(Color.BLACK);
                    canvas.drawText(l.value+"",cx+convertDpToPixel(20),cy+convertDpToPixel(6),paint);
                }
                if (l.side == 1 || l.side == 3)
                {
                    cx = (int) (p1.x - (b2.getWidth() / 2));
                    cy = (int) ((p1.y + p2.y) / 2) - (b2.getHeight() / 2);

                    paint.setTextSize(20);
                    paint.setColor(Color.BLACK);
                    canvas.drawText(l.value+"",cx-convertDpToPixel(1),cy+convertDpToPixel(20),paint);
                }
                canvas.drawBitmap(b2, cx, cy, paint);

            }
        }
    }

    public Bitmap RotateBitmap(Bitmap source, float angle)
    {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }

    public float convertDpToPixel(float dp)
    {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * ((float) metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return px;
    }
}
