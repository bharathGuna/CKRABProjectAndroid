package com.finalproject.cs4962.whale;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.MotionEvent;
import android.widget.ImageView;

public class CircularImageView extends ImageView
{
    public enum ProfileMode
    {
        MYPROFILE,
        FRIENDPROFILE
    }

	// Border & Selector configuration variables
	private boolean hasBorder;
	private int borderWidth;
	private int canvasSize;
    private boolean hasShadow;
	private String name;

    ProfileMode mode;
	// Objects used for the actual drawing
	private BitmapShader shader;
	private Bitmap image;
	private Paint paint;
	private Paint paintBorder;
	private static final Bitmap.Config BITMAP_CONFIG = Bitmap.Config.ARGB_8888;
	private static final int COLORDRAWABLE_DIMENSION = 2;


	public CircularImageView(Context context,ProfileMode _mode)
	{
        super(context);
		mode = _mode;
        init(context);
	}

    public CircularImageView(Context context)
    {
        this(context, ProfileMode.MYPROFILE);

    }



	/**
	 * Initializes paint objects and sets desired attributes.
	 * 
	 * @param context
	 */
	private void init(Context context)
	{
		super.setScaleType(ScaleType.CENTER_CROP);
		// Initialize paint objects
		paint = new Paint();
		paint.setAntiAlias(true);
		paintBorder = new Paint();
		paintBorder.setAntiAlias(true);
		

		
		// Check if border and/or border is enabled
		hasBorder = true;
		
		// Set border properties if enabled
		if(hasBorder) {
			int defaultBorderSize = (int) (2 * context.getResources().getDisplayMetrics().density + 0.5f);
			setBorderWidth(10);
			setBorderColor(Color.WHITE);
		}

		// Add shadow if enabled
		if(hasShadow)
			addShadow();
	}

	@Override
	public void setImageResource( int resId) {
		super.setImageResource(resId);
		image = getBitmapFromDrawable(getDrawable());

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
			paintBorder.setColor(_borderColor);
		invalidate();
	}

	
	/**
	 * Adds a dark shadow to this CircularImageView.
	 */
	public void addShadow()
	{
		setLayerType(LAYER_TYPE_SOFTWARE, paintBorder);
		paintBorder.setShadowLayer(10.0f, 3.0f, 2.0f, Color.BLACK);
	}
	
	
	@Override
	public void onDraw(Canvas canvas)
	{
		// Don't draw anything without an image
		if(image == null)
			return;
		
		// Nothing to draw (Empty bounds)
		if(image.getHeight() == 0 || image.getWidth() == 0)
			return;
		
		// Compare canvas sizes
		int oldCanvasSize = canvasSize;
		
		canvasSize = canvas.getWidth();
		if(canvas.getHeight() < canvasSize)
			canvasSize = canvas.getHeight();
		
		// Reinitialize shader, if necessary
		if(oldCanvasSize != canvasSize)
			refreshBitmapShader();
		
		// Apply shader to paint
		paint.setShader(shader);
		
		// Keep track of selectorStroke/border width
		int outerWidth = 10;
		
		// Get the exact X/Y axis of the view
		int center = canvasSize / 2;
		

        if(hasBorder) { // If no selector was drawn, draw a border and clear the filter instead... if enabled
			outerWidth = borderWidth;
			center = (canvasSize - (outerWidth * 2)) / 2;
			
			paint.setColorFilter(null);
			canvas.drawCircle(center + outerWidth, center + outerWidth, ((canvasSize - (outerWidth * 2)) / 2) + outerWidth - 4.0f, paintBorder);
		}
		else // Clear the color filter if no selector nor border were drawn
			paint.setColorFilter(null);
		
		// Draw the circular image itself
		canvas.drawCircle(center + outerWidth, center + outerWidth, ((canvasSize - (outerWidth * 2)) / 2) - 4.0f, paint);
	}

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        if(mode == ProfileMode.MYPROFILE)
        {
            setBorderColor(Color.YELLOW);
        }
        return super.onTouchEvent(event);
    }

    @Override
	public void invalidate() {
		super.invalidate();
		image = drawableToBitmap(getDrawable());
		if(shader != null || canvasSize > 0)
			refreshBitmapShader();
	}


	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
	{
		int width = measureWidth(widthMeasureSpec);
		int height = measureHeight(heightMeasureSpec);
		setMeasuredDimension(width, height);
	}

	private int measureWidth(int measureSpec)
	{
		int result = 0;
		int specMode = MeasureSpec.getMode(measureSpec);
		int specSize = MeasureSpec.getSize(measureSpec);

		if (specMode == MeasureSpec.EXACTLY) {
			// The parent has determined an exact size for the child.
			result = specSize;
		}
		else if (specMode == MeasureSpec.AT_MOST) {
			// The child can be as large as it wants up to the specified size.
			result = specSize;
		}
		else {
			// The parent has not imposed any constraint on the child.
			result = canvasSize;
		}

		return result;
	}

	private int measureHeight(int measureSpecHeight)
	{
		int result = 0;
		int specMode = MeasureSpec.getMode(measureSpecHeight);
		int specSize = MeasureSpec.getSize(measureSpecHeight);

		if (specMode == MeasureSpec.EXACTLY) {
			// We were told how big to be
			result = specSize;
		} else if (specMode == MeasureSpec.AT_MOST) {
			// The child can be as large as it wants up to the specified size.
			result = specSize;
		} else {
			// Measure the text (beware: ascent is a negative number)
			result = canvasSize;
		}

		return (result + 2);
	}

	/**
	 * Convert a drawable object into a Bitmap
	 * 
	 * @param drawable
	 * @return
	 */
	public Bitmap drawableToBitmap(Drawable drawable)
	{
		if (drawable == null) { // Don't do anything without a proper drawable
			return null;
		}
		else if (drawable instanceof BitmapDrawable) { // Use the getBitmap() method instead if BitmapDrawable
			return ((BitmapDrawable) drawable).getBitmap();
		}
		
		// Create Bitmap object out of the drawable
		Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmap);
		drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
		drawable.draw(canvas);
		
		return bitmap;
	}
	
	/**
	 * Reinitializes the shader texture used to fill in 
	 * the Circle upon drawing.
	 */
	public void refreshBitmapShader()
	{
		shader = new BitmapShader(Bitmap.createScaledBitmap(image, canvasSize, canvasSize, false), Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
	}

}