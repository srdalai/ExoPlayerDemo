package com.sdtechnocrat.exoplayerdemo;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.text.style.ReplacementSpan;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class SubBackgroundSpan extends ReplacementSpan {
    private static final float PADDING_LEFT = 20.0f;
    private static final float PADDING_RIGHT = 40.0f;
    private static final float PADDING_TOP = 2.0f;
    private static final float PADDING_BOTTOM = 4.0f;
    private static final float TEXT_X = 20.0f;
    private static final float TEXT_Y = 2.0f;

    Context mContext;

    public SubBackgroundSpan(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public int getSize(@NonNull Paint paint, CharSequence text, int start, int end, @Nullable Paint.FontMetricsInt fontMetricsInt) {
        return Math.round(MeasureText(paint, text, start, end) + PADDING_RIGHT);
    }

    @Override
    public void draw(@NonNull Canvas canvas, CharSequence text, int start, int end, float x, int top, int y, int bottom, @NonNull Paint paint) {
        RectF rect = new RectF(x - PADDING_LEFT, top + PADDING_TOP, x + MeasureText(paint, text, start, end) + PADDING_RIGHT, bottom - PADDING_BOTTOM);
        paint.setColor(mContext.getResources().getColor(R.color.sub_bg_color));
        canvas.drawRect(rect, paint);
        paint.setColor(mContext.getResources().getColor(R.color.white));
        canvas.drawText(text, start, end, x + TEXT_X, y + TEXT_Y, paint);
    }

    private float MeasureText(Paint paint, CharSequence text, int start, int end) {
        return paint.measureText(text, start, end);
    }
}
