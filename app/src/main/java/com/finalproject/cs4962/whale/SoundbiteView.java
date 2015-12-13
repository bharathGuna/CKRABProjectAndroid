package com.finalproject.cs4962.whale;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

public class SoundbiteView extends View
{
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

        Paint boundsPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        boundsPaint.setColor(getResources().getColor(R.color.colorAccent));
        boundsPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        float roundRadius = getHeight() * 0.1f;
        canvas.drawRoundRect(bounds, roundRadius, roundRadius, boundsPaint);
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
