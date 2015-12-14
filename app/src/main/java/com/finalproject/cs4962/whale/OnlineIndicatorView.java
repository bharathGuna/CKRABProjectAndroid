package com.finalproject.cs4962.whale;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Bharath on 12/13/2015.
 */
public class OnlineIndicatorView extends View
{
    private int canvasSize;
    private boolean state;
    private Paint paint;

    public OnlineIndicatorView(Context context)
    {
        super(context);
        init();
    }
    public OnlineIndicatorView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init();
    }

    private void init()
    {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(getResources().getDisplayMetrics().density);
    }

    public void setState(boolean _state)
    {
        state = _state;
        invalidate();
    }

    public boolean getState()
    {
        return state;
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);
        canvasSize = canvas.getWidth();

        if(canvas.getHeight() < canvasSize)
            canvasSize = canvas.getHeight();

        int padding = (int) (4.0f * getResources().getDisplayMetrics().density);
        canvasSize -= 2 * padding;
        int heightPadding = padding;

        RectF picture = new RectF();
        picture.left = padding;
        picture.top = heightPadding;
        picture.right = padding + canvasSize;
        picture.bottom = getHeight() - heightPadding;

        if(state)
            paint.setColor(Color.GREEN);
        else
            paint.setColor(Color.TRANSPARENT);

        canvas.drawOval(picture,paint);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
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
