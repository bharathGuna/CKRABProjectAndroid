package com.finalproject.cs4962.whale;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;


public class WaveView extends View implements ValueAnimator.AnimatorUpdateListener
{
    private int minHeight = (int) getResources().getDisplayMetrics().density * 40;
    private int minWidth = minHeight * 6;
    private RectF playRect = new RectF();
    private RectF waveRect = new RectF();
    private ValueAnimator animator = new ValueAnimator();
    private int length = 5;
    private float percentage = 0.5f;

    public WaveView(Context context)
    {
        super(context);
        init();
    }

    public WaveView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    private void init()
    {
        setMinimumHeight(minHeight);
        setMinimumWidth(minWidth);
        animator.setIntValues(0, 100);
        animator.setDuration(length * 1000);
        animator.addUpdateListener(this);
        //setBackgroundColor(Color.WHITE);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {

        PointF touchPoint = new PointF(event.getX(), event.getY());
        if (touchPoint.x > playRect.left && touchPoint.x < playRect.right &&
                touchPoint.y > playRect.top && touchPoint.y < playRect.bottom)
        {
            if (!animator.isStarted())
                animator.start();
        }
        else
        {
            float scaledX = touchPoint.x - playRect.width();
            percentage = scaledX / (waveRect.width());
            //animator.setCurrentFraction(percentage); // requires api 22
            animator.setIntValues((int)(percentage * 100), 100);
            invalidate();
        }

        if (animator.isRunning())
        {
            if (event.getAction() == MotionEvent.ACTION_UP)
            {
                animator.cancel();
            }
        }


        return true;
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
        {
            width = widthSpec;
            height = width / 6;
        }
        if (heightMode == MeasureSpec.AT_MOST)
        {
            height = heightSpec;
            width = height * 6;
        }

        if (heightMode == MeasureSpec.EXACTLY)
        {
            height = heightSpec;
            width = height * 6;
        }

        if (widthMode == MeasureSpec.EXACTLY)
        {
            width = widthSpec;
            height = width / 6;
        }

        if (width > 6 * height && widthMode != MeasureSpec.EXACTLY)
            width = 6 * height;
        if (height > width / 6 && heightMode != MeasureSpec.EXACTLY)
            height = width / 6;

        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        // 2 seconds is 1 period
        super.onDraw(canvas);

        int pad = (int) (4.0f * getResources().getDisplayMetrics().density);
        playRect.left = getPaddingLeft() + pad;
        playRect.top = getPaddingTop() + pad;
        playRect.right = getHeight() - pad;
        playRect.bottom = getHeight() - getPaddingBottom() - pad;

        Paint circlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        circlePaint.setStyle(Paint.Style.STROKE);
        circlePaint.setStrokeWidth(0.05f * getHeight());
        circlePaint.setColor(getResources().getColor(R.color.colorAccent));

        float radius = playRect.width() / 2;
        float centerX = playRect.left + radius;
        float centerY = playRect.top + radius;
        float triRadius = 0.75f * radius;

        PointF right = new PointF();
        right.x = centerX + (float) (triRadius * Math.cos(0));
        right.y = centerY + (float) (triRadius * Math.sin(0));

        PointF top = new PointF();
        top.x = centerX + (float) (triRadius * Math.cos(Math.PI * 2 / 3));
        top.y = centerY + (float) (triRadius * Math.sin(Math.PI * 2 / 3));

        PointF bot = new PointF();
        bot.x = top.x;
        bot.y = centerY + (float) (triRadius * Math.sin(-Math.PI * 2 / 3));

        Paint triPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        triPaint.setStyle(Paint.Style.FILL);
        triPaint.setColor(getResources().getColor(R.color.textColorPrimary));

        Path triPath = new Path();
        triPath.moveTo(right.x, right.y);
        triPath.lineTo(top.x, top.y);
        triPath.lineTo(bot.x, bot.y);
        triPath.lineTo(right.x, right.y);

        Paint wavePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        wavePaint.setStyle(Paint.Style.STROKE);
        wavePaint.setStrokeWidth(0.01f * getHeight());
        wavePaint.setDither(true);
        wavePaint.setColor(getResources().getColor(R.color.textColorPrimary));


        Paint playPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        playPaint.set(wavePaint);
        playPaint.setStrokeWidth(0.05f * getHeight());
        playPaint.setColor(getResources().getColor(R.color.colorAccent) & 0xCAFFFFFF);

        waveRect.left = playRect.right;
        waveRect.top = playRect.top + pad;
        waveRect.right = getWidth() - getPaddingRight() - pad;
        waveRect.bottom = getHeight() - getPaddingBottom() - pad;

        Path wavePath = new Path();
        Path playPath = new Path();
        int totalPlayPathPoints = (int) (percentage * 100);
        float periods = 6;
        float deltaX = (waveRect.width()) / 100;
        float scaleY = waveRect.height() / 2;
        PointF start = new PointF(waveRect.left, waveRect.centerY());
        for (int i = 0; i < 100; i++)
        {
            float x = start.x + i * deltaX;
            float y = start.y - (float) (scaleY * Math.sin((double) i / (double) 100 * periods * 2 * Math.PI));

            if (i == 0)
            {
                wavePath.moveTo(start.x, start.y);
                if (i <= totalPlayPathPoints)
                    playPath.moveTo(start.x, start.y);
            }
            else
            {

                wavePath.lineTo(x, y);
                if (i <= totalPlayPathPoints)
                    playPath.lineTo(x, y);
            }
        }

//        float scaledWidth = waveRect.width();
//        float stepX =

        canvas.drawPath(playPath, playPaint);
        canvas.drawPath(wavePath, wavePaint);
        canvas.drawPath(triPath, triPaint);
        canvas.drawOval(playRect, circlePaint);

    }

    @Override
    public void onAnimationUpdate(ValueAnimator valueAnimator)
    {
        int index = (Integer) valueAnimator.getAnimatedValue();
        percentage = (float)index/(float)100;
        invalidate();
    }
}
