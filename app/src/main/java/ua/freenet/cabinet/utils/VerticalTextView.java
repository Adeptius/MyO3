package ua.freenet.cabinet.utils;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Region;

public class VerticalTextView extends android.support.v7.widget.AppCompatTextView{

    public VerticalTextView(Context context) {
        super(context);
    }

    @Override
    protected void onMeasure(int widthSpec, int heightSpec) {
        super.onMeasure(heightSpec, widthSpec);
        setMeasuredDimension(getMeasuredHeight(), getMeasuredWidth());
    }

    @Override
    protected boolean setFrame(int left, int top, int right, int bottom) {
        super.setFrame(left, top, left + (bottom - top), top + (right - left));
        return true;
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.translate(0, getWidth());
        canvas.rotate(-90);
        canvas.clipRect(0, 0, getWidth(), getHeight(), Region.Op.REPLACE);
        super.draw(canvas);
    }
}
