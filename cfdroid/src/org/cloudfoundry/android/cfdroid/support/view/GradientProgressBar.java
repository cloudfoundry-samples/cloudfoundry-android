package org.cloudfoundry.android.cfdroid.support.view;

import java.util.ArrayList;
import java.util.List;

import org.cloudfoundry.android.cfdroid.R;
import org.cloudfoundry.android.cfdroid.support.Colors;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

/**
 * A simple linear progress bar
 * 
 * @author Eric Bottard
 * 
 */
public class GradientProgressBar extends View {

	private int midPoint = 50;

	private int startColor = Color.GREEN;

	private int midColor = Color.YELLOW;

	private int endColor = Color.RED;
	
	private Paint paint = new Paint();

	private Rect rect = new Rect();

	private int progress;

	public GradientProgressBar(Context context) {
		super(context);
		setProgress(0);
	}

	public GradientProgressBar(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);

		TypedArray a = context.obtainStyledAttributes(attrs,
				R.styleable.GradientProgressBar);
		try {
			startColor = a.getColor(
					R.styleable.GradientProgressBar_start_color, Color.GREEN);
			midColor = a.getColor(R.styleable.GradientProgressBar_mid_color,
					Color.YELLOW);
			endColor = a.getColor(R.styleable.GradientProgressBar_end_color,
					Color.RED);
			midPoint = a
					.getInt(R.styleable.GradientProgressBar_mid_point, 50);
		} finally {
			a.recycle();
		}
		setProgress(20);
	}

	public GradientProgressBar(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}
	
	public void setProgress(int progress) {
		this.progress = progress;
		
		int left = progress < midPoint ? startColor : midColor;
		int right = progress < midPoint ? midColor : endColor;
		float p = progress < midPoint ? ((float)progress/midPoint) : ((float)(progress-midPoint)/(100-midPoint));
		
		paint.setColor(Colors.blend(left, right, p));

	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		
		int w = getMeasuredWidth() * progress / 100;
		rect.set(0, 0, w, getMeasuredHeight());

	}
	

	@Override
	protected void onDraw(Canvas canvas) {
		canvas.drawRect(rect, paint);
	}

}
