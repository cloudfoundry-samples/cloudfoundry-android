package org.cloudfoundry.android.cfdroid.support;

/**
 * Base class for views that can populate themselves from an item of kind I.
 * 
 * @author ebottard
 * 
 * @param <I>
 *            the kind of item this view supports.
 */
public abstract class BaseViewHolder<I> {

	public abstract void bind(I item);
	
	public void bind(I item, int position) {
		bind(item);
	}
	
}
