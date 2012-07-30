package org.cloudfoundry.android.cfdroid.support;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * A {@link ViewPager} that actually doesn't support swiping. This may seem odd
 * but is useful if one wants to benefit from <i>eg.<i/> a TitlePageIndicator
 * but doesn't want swipping (for example because it interferes with come
 * controls, like seekbars).
 * 
 * @author Eric Bottard
 * 
 */
public class NoSwippingViewPager extends ViewPager {

	public NoSwippingViewPager(Context context) {
		super(context);
	}

	public NoSwippingViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return false;
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent event) {
		return false;
	}
}