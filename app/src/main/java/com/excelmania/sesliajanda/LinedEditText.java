package com.excelmania.sesliajanda;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.EditText;

@SuppressLint("AppCompatCustomView")
public class LinedEditText extends EditText {
    Context cx;
    private Paint mPaint = new Paint();
    private Rect mRect = new Rect();

    public LinedEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mPaint.setStyle(Style.STROKE);
        this.mPaint.setColor(Color.rgb(175, 238, 238));
        this.cx = context;
    }

    /* Access modifiers changed, original: protected */
    public void onDraw(Canvas canvas) {
        int count = getLineCount();
        Rect r = this.mRect;
        Paint paint = this.mPaint;
        for (int i = 0; i < count; i++) {
            int baseline = getLineBounds(i, r);
            canvas.drawLine((float) r.left, (float) (baseline + 1), (float) r.right, (float) (baseline + 1), paint);
        }
        super.onDraw(canvas);
    }
}