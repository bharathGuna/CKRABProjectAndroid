package com.finalproject.cs4962.whale;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.widget.Button;

/**
 * Created by Bharath on 11/29/2015.
 * The purpose of this class is allow for a button that can have symbols drawn on it.
 * The symbol to be draw will be passed in through a method.
 *
 */
public class DrawButton extends Button
{


    public interface DrawSymbol
    {
        void draw(Canvas canvas);
    }

    private DrawSymbol drawSymbol = null;

    public void setDrawSymbol(DrawSymbol _drawSymbol)
    {
        drawSymbol = _drawSymbol;
        this.setBackgroundColor(Color.TRANSPARENT);
    }

    public DrawButton(Context context)
    {
        super(context);
    }

    public DrawButton(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }
    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);

        if(drawSymbol != null)
        {
            drawSymbol.draw(canvas);
        }

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        int widthMod = MeasureSpec.getMode(widthMeasureSpec);
        int heightMod = MeasureSpec.getMode(heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        int measureWidth = width  ;
        int measureHeight = height ;
        int widthSize = (int) (getResources().getDisplayMetrics().density * 40);


        if(widthMod == MeasureSpec.AT_MOST && heightMod == MeasureSpec.AT_MOST)
        {
            measureWidth = Math.min(Math.min(width,height),widthSize);
            measureHeight = measureWidth;
        }

        if(widthMod == MeasureSpec.AT_MOST && heightMod == MeasureSpec.EXACTLY)
        {
            if(height > widthSize)
            {
                measureHeight = height;
                measureWidth = measureHeight;
            }
            else
            {
                measureWidth = Math.min(width, height);
                measureHeight = height;

            }
        }
        if(widthMod == MeasureSpec.EXACTLY && heightMod == MeasureSpec.AT_MOST)
        {
            if(width > widthSize)
            {
                measureHeight = width;
                measureWidth = width;
            }
            else
            {
                measureWidth = width;
                measureHeight = Math.min(width, height);
            }
        }


        if(measureWidth < widthSize )
        {
            measureWidth |= MEASURED_STATE_TOO_SMALL;
        }

        if(measureHeight < widthSize)
            measureHeight |= MEASURED_STATE_TOO_SMALL;

        setMeasuredDimension(measureWidth, measureHeight);
    }
}
