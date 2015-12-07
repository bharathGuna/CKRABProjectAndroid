package com.finalproject.cs4962.whale;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * CircularImageView takes in a picture and represents the picture as a circle.
 * You can add pictures of any size and it will resize the image to fit the view size
 * You can change the board color and width as you like
 */
public class CircularImageView extends ImageView
{


	// Border & Selector configuration variables
	private boolean hasBorder;
	private int borderWidth;
	private int canvasSize;
    private boolean hasShadow;
	private String name;

    Boolean myProfile;
	// Objects used for the actual drawing
	private Bitmap image;
	private Paint paintBorder;
	private Paint textPaint;
	private static final Bitmap.Config BITMAP_CONFIG = Bitmap.Config.ARGB_8888;
	private static final int COLORDRAWABLE_DIMENSION = 2;


	public CircularImageView(Context context)
	{
        super(context);
        init(context);

	}

	public CircularImageView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		init(context);
	}


	/**
	 * Initializes paint objects and sets desired attributes.
	 * 
	 * @param context
	 */
	private void init(Context context)
	{
		// Initialize paint objects
		paintBorder = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintBorder.setStyle(Paint.Style.STROKE);

		
		// Check if border and/or border is enabled
		hasBorder = true;
		
		// Set border properties if enabled
		if(hasBorder) {
			int defaultBorderSize = (int) (2 * context.getResources().getDisplayMetrics().density + 0.5f);
			setBorderWidth(defaultBorderSize);
			setBorderColor(Color.WHITE);
		}

	}

	
	@Override
	public void onDraw(Canvas canvas)
	{

		canvasSize = canvas.getWidth();

		if(canvas.getHeight() < canvasSize)
			canvasSize = canvas.getHeight();

        int padding = (int) (4.0f * getResources().getDisplayMetrics().density);//(int)(canvasSize * .125f);
        canvasSize -= 2 * padding;
        int heightPadding = padding;

        RectF picture = new RectF();
        picture.left = padding;
        picture.top = heightPadding;
        picture.right = padding + canvasSize;
        picture.bottom = getHeight() - heightPadding;


		if(image != null)
		{
			prepImageForDrawing();
			// Apply shader to paint
			canvas.drawBitmap(image, picture.left, picture.top, paintBorder);
		}

		if(name != null && image == null)
		{
			String initial = ""+name.charAt(0);
			drawCenter(canvas,initial);
		}
        if(hasBorder)
        canvas.drawOval(picture,paintBorder);

	}

	private void drawCenter(Canvas canvas, String text) {
		int cHeight = canvas.getClipBounds().height();
		int cWidth = canvas.getClipBounds().width();
		Rect r = new Rect();
		textPaint.setTextAlign(Paint.Align.LEFT);
		textPaint.setTextSize(canvasSize * .8f);
		textPaint.getTextBounds(text, 0, text.length(), r);

		textPaint.setColor(Color.WHITE);
		float x = cWidth / 2f - r.width() / 2f - r.left;
		float y = cHeight / 2f + r.height() / 2f - r.bottom;
		canvas.drawText(text, x, y, textPaint);
	}

	//crops the image into a circle shape
    public static Bitmap getCircleCrop(Bitmap bitmap) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
				bitmap.getHeight(), BITMAP_CONFIG);
        Canvas canvas = new Canvas(output);

        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        canvas.drawCircle(bitmap.getWidth() / 2, bitmap.getHeight() / 2,
				bitmap.getWidth() / 2, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;
    }

	/**
	 * Crops the image into a circle and scales the image to the view size
	 */
	public void prepImageForDrawing()
	{
		if(image != null)
		{
			image = Bitmap.createScaledBitmap(image, canvasSize, canvasSize, false);
			image = getCircleCrop(image);
		}

	}


	//Getters and Setters for variables

	public void setBorder(boolean border)
	{
		hasBorder = border;
	}
	public void setMyProfile(boolean profile)
	{
		myProfile = profile;
	}
	public String getName()
	{
		return name;
	}

	public void setName(String _name)
	{
		name = _name;
		if(textPaint == null)
		{
			textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
			textPaint.setStyle(Paint.Style.FILL);
		}
		invalidate();
	}
	public void setShadow(Boolean _hasShadow)
	{
		hasShadow = _hasShadow;
	}
	public Bitmap getImage()
	{
		return image;
	}
	@Override
	public void setImageResource( int resId) {
		super.setImageResource(resId);
		image = getBitmapFromDrawable(getDrawable());
		invalidate();

	}

	@Override
	public void setImageBitmap(Bitmap bm)
	{
		super.setImageBitmap(bm);
		image = bm;
		invalidate();
	}

	private Bitmap getBitmapFromDrawable(Drawable drawable) {
		if (drawable == null) {
			return null;
		}

		if (drawable instanceof BitmapDrawable) {
			return ((BitmapDrawable) drawable).getBitmap();
		}

		try {
			Bitmap bitmap;

			if (drawable instanceof ColorDrawable) {
				bitmap = Bitmap.createBitmap(COLORDRAWABLE_DIMENSION, COLORDRAWABLE_DIMENSION, BITMAP_CONFIG);
			} else {
				bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), BITMAP_CONFIG);
			}

			Canvas canvas = new Canvas(bitmap);
			drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
			drawable.draw(canvas);
			return bitmap;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	/**
	 * Sets the CircularImageView's border width in pixels.
	 *
	 * @param _borderWidth
	 */
	public void setBorderWidth(int _borderWidth)
	{
		borderWidth = _borderWidth;
		paintBorder.setStrokeWidth(borderWidth);
		requestLayout();
		invalidate();
	}

	/**
	 * Sets the CircularImageView's basic border color.
	 *
	 * @param _borderColor
	 */
	public void setBorderColor(int _borderColor)
	{
		if (paintBorder != null)
		{
			paintBorder.setColor(_borderColor);
			invalidate();
		}
	}


	public void setTextPaintColor(int _color)
	{
		if(textPaint != null)
		{
			textPaint.setColor(_color);
			invalidate();
		}
	}

	public int getCanvasSize()
	{
		return canvasSize;
	}
	@Override
	public void invalidate() {
		super.invalidate();
		if(image != null && canvasSize > 0)
			prepImageForDrawing();
	}


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
//		super.onMeasure(widthMeasureSpec,heightMeasureSpec);
//        int widthMod = MeasureSpec.getMode(widthMeasureSpec);
//        int heightMod = MeasureSpec.getMode(heightMeasureSpec);
//        int width = MeasureSpec.getSize(widthMeasureSpec);
//        int height = MeasureSpec.getSize(heightMeasureSpec);
//        int measureWidth = width  ;
//        int measureHeight = height ;
//        int widthSize = (int) (getResources().getDisplayMetrics().density * 140);
//
//
//        if(widthMod == MeasureSpec.AT_MOST && heightMod == MeasureSpec.AT_MOST)
//        {
//            measureWidth = Math.min(Math.min(width,height),widthSize);
//			measureHeight = measureWidth;
//        }
//
//        if(widthMod == MeasureSpec.AT_MOST && heightMod == MeasureSpec.EXACTLY)
//        {
//            if(height > widthSize)
//            {
//                measureHeight = height;
//                measureWidth = measureHeight;
//            }
//            else
//            {
//                measureWidth = Math.min(width, height);
//                measureHeight = height;
//
//            }
//        }
//        if(widthMod == MeasureSpec.EXACTLY && heightMod == MeasureSpec.AT_MOST)
//        {
//            if(width > widthSize)
//            {
//                measureHeight = width;
//                measureWidth = width;
//            }
//            else
//            {
//                measureWidth = width;
//                measureHeight = Math.min(width, height);
//            }
//        }
//
//		if(widthMod == MeasureSpec.EXACTLY && heightMod == MeasureSpec.EXACTLY)
//		{
//			measureWidth = Math.min(Math.min(width,height),widthSize);
//			measureHeight = measureWidth;
//		}
//
//
//        if(measureWidth < widthSize )
//        {
//            measureWidth |= MEASURED_STATE_TOO_SMALL;
//        }
//
//        if(measureHeight < widthSize)
//            measureHeight |= MEASURED_STATE_TOO_SMALL;
//
//        setMeasuredDimension(measureWidth, measureHeight);
		// Measure the cell, keep it square
		// Start from the smallest size and build up

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