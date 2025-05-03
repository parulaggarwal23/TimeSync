package com.example.timesync;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

public class CircularProgressView extends View {
    private int progress = 0; // 0-100
    private int progressColor = Color.parseColor("#F44336");
    private Paint arcPaint, textPaint;
    private RectF arcRect;

    public CircularProgressView(Context context) {
        super(context);
        init();
    }
    public CircularProgressView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }
    private void init() {
        arcPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        arcPaint.setStyle(Paint.Style.STROKE);
        arcPaint.setStrokeWidth(6f);
        arcPaint.setStrokeCap(Paint.Cap.ROUND);
        arcPaint.setColor(progressColor);

        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setColor(progressColor);
        textPaint.setTextSize(18f);
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setFakeBoldText(true);
    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int w = getWidth();
        int h = getHeight();
        int size = Math.min(w, h);
        if (arcRect == null) {
            float pad = 8f;
            arcRect = new RectF(pad, pad, size - pad, size - pad);
        }
        float sweep = (progress / 100f) * 270f;
        canvas.drawArc(arcRect, 135, sweep, false, arcPaint);
        canvas.drawText(progress + "%", w / 2f, h / 2f + 8f, textPaint);
    }
    public void setProgress(int progress) {
        this.progress = progress;
        invalidate();
    }
    public void setProgressColor(int color) {
        this.progressColor = color;
        arcPaint.setColor(color);
        textPaint.setColor(color);
        invalidate();
    }
} 