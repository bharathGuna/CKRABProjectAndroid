package com.finalproject.cs4962.whale;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.TextView;

/**
 * Created by Bharath on 11/27/2015.
 */
public class CustomTextView extends TextView
{

    private Paint paint;
    boolean name;
    private int lineCount = 1;
    private Handler handler = new Handler();



    public CustomTextView(Context context, String text, boolean _name)
    {
        this(context);

        name = _name;
        if (name)
        {
            paint = new Paint(Paint.ANTI_ALIAS_FLAG);
            paint.setStyle(Paint.Style.STROKE);
            paint.setColor(getResources().getColor(R.color.colorAccent));
            lineCount = 2;
        }
        setLines(lineCount);
        setTextAlignment(TEXT_ALIGNMENT_CENTER);
        setText(text);
    }

    public CustomTextView(Context context)
    {
        super(context);
        this.setTextColor(Color.WHITE);
        setGravity(Gravity.CENTER);
    }

    public CustomTextView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }


    private void setUnderlineColor(int color)
    {
        paint.setColor(color);
        invalidate();
    }

    public void setViewText(String text)
    {
        setText(text);
        calculateOptimalTextSize();
    }

    private void calculateOptimalTextSize()
    {
        String text = (String) getText();
        int width = getWidth();
        int height = getHeight();
        if (width == 0 || height == 0)
            return;
        int lineSize = getLineHeight();
        int lines = getLineCount();
        float textSize = getTextSize();
        int totalHeight = lineSize * lines;

        int size = pixelsToSp(textSize);

        int totalWidth = (int)getPaint().measureText(text);
        //currentSize is too big
        if (totalHeight > height && totalWidth > width)
        {
            while (totalHeight > height && totalWidth > width)
            {
                size--;
                setTextSize(size);
                totalHeight = getLineHeight() * lineCount;
                totalWidth = (int)getPaint().measureText(text);

            }
        }
        //textSize is too small
        else
        {
            while (totalHeight < height && totalWidth < width)
            {
                size++;
                setTextSize(size);
                totalHeight = getLineHeight() * lineCount;
                totalWidth = (int)getPaint().measureText(text);
            }

        }
    }


    public int spToPixels(float sp)
    {
        float scaledDensity = getResources().getDisplayMetrics().scaledDensity;
        return (int) (sp * scaledDensity);
    }

    public int pixelsToSp(float px)
    {
        float scaledDensity = getResources().getDisplayMetrics().scaledDensity;
        return (int) (px / scaledDensity);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh)
    {
        super.onSizeChanged(w, h, oldw, oldh);
        calculateOptimalTextSize();
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);

        if (name)
        {
            int lineSize = getWidth() / 4;
            paint.setStrokeWidth(getResources().getDisplayMetrics().density);
            canvas.drawLine(lineSize, getHeight(), lineSize * 3, getHeight(), paint);

        }

    }


}
