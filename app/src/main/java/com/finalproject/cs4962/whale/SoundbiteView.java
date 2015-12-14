package com.finalproject.cs4962.whale;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

public class SoundbiteView extends View
{
    private boolean selected = false;

    public SoundbiteView(Context context)
    {
        super(context);
        init();
    }

    public SoundbiteView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init();
    }

    public void select()
    {
        selected = !selected;
        invalidate();
    }

    private void init()
    {
        int size = (int) (80f * getResources().getDisplayMetrics().density);
        setMinimumHeight(size);
        setMinimumWidth(size);
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);

        RectF bounds = new RectF();
        bounds.left = getPaddingLeft();
        bounds.top = getPaddingTop();
        bounds.right = getWidth() - getPaddingRight();
        bounds.bottom = getHeight() - getPaddingBottom();

        float borderThickness = getResources().getDisplayMetrics().density * 2.0f;
        RectF inner = new RectF(bounds);
        inner.left += borderThickness;
        inner.top += borderThickness;
        inner.right -= borderThickness;
        inner.bottom -= borderThickness;

        float centerX = inner.left + inner.width() / 2;
        float centerY = inner.top + inner.height() / 2;
        float triRadius = 0.6f * inner.width();

        PointF right = new PointF();
        right.x = centerX + triRadius / 2 + triRadius / 10;
        right.y = centerY;

        PointF top = new PointF();
        top.x = centerX - triRadius / 2;
        top.y = centerY - triRadius / 2;

        PointF bot = new PointF();
        bot.x = top.x;
        bot.y = centerY + triRadius / 2;

        Path triangle = new Path();
        triangle.moveTo(right.x, right.y);
        triangle.lineTo(top.x, top.y);
        triangle.lineTo(bot.x, bot.y);
        triangle.lineTo(right.x, right.y);

        Paint boundsPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        boundsPaint.setColor(getResources().getColor(R.color.colorAccent));
        boundsPaint.setStyle(Paint.Style.FILL_AND_STROKE);

        Paint innerPaint = new Paint(boundsPaint);
        innerPaint.setColor(getResources().getColor(R.color.background_material_light));

        Paint trianglePaint = new Paint(innerPaint);
        if (!selected)
            trianglePaint.setColor(getResources().getColor(R.color.textColorPrimary));
        else
            trianglePaint.setColor(getResources().getColor(R.color.colorAccent));

        float roundRadius = getHeight() * 0.1f;

        canvas.drawRoundRect(bounds, roundRadius, roundRadius, boundsPaint);
        canvas.drawRoundRect(inner, roundRadius, roundRadius, innerPaint);
        canvas.drawPath(triangle, trianglePaint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int widthSpec = MeasureSpec.getSize(widthMeasureSpec);
        int heightSpec = MeasureSpec.getSize(heightMeasureSpec);

        int width = getSuggestedMinimumWidth();
        int height = getSuggestedMinimumHeight();

        if (widthMode == MeasureSpec.AT_MOST)
            width = widthSpec;
        if (heightMode == MeasureSpec.AT_MOST)
            height = heightSpec;

        if (widthMode == MeasureSpec.EXACTLY)
        {
            width = widthSpec;
            height = width;
        }
        if (heightMode == MeasureSpec.EXACTLY)
        {
            height = heightSpec;
            width = height;
        }

        if (width > height && widthMode != MeasureSpec.EXACTLY)
            width = height;
        if (height > width && heightMode != MeasureSpec.EXACTLY)
            height = width;

        setMeasuredDimension(width, height);
    }
}
