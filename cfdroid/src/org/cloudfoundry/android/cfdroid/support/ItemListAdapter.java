package org.cloudfoundry.android.cfdroid.support;

import java.util.ArrayList;
import java.util.List;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

/**
 * List adapter for items of a specific type.
 * 
 * @param <I>
 *            the kind of domain object this adapter deals with
 * @param <V>
 *            the type that represents an item's view
 */
public abstract class ItemListAdapter<I, V extends BaseViewHolder<I>> extends
		BaseAdapter {

	private final LayoutInflater inflater;

	private final int viewId;

	private List<I> elements;

	/**
	 * Create empty adapter
	 * 
	 * @param viewId
	 * @param inflater
	 */
	public ItemListAdapter(final int viewId, final LayoutInflater inflater) {
		this(viewId, inflater, new ArrayList<I>());
	}

	/**
	 * Create adapter
	 * 
	 * @param viewId
	 * @param inflater
	 * @param elements
	 */
	public ItemListAdapter(final int viewId, final LayoutInflater inflater,
			final List<I> elements) {
		this.viewId = viewId;
		this.inflater = inflater;
		this.elements = elements;
	}

	@Override
	public boolean hasStableIds() {
		return true;
	}

	public int getCount() {
		return elements.size();
	}

	public I getItem(int position) {
		return elements.get(position);
	}

	public long getItemId(int position) {
		return getItem(position).hashCode();
	}

	/**
	 * Set items
	 * 
	 * @param items
	 * @return items
	 */
	public ItemListAdapter<I, V> setItems(List<I> items) {
		elements = items;
		notifyDataSetChanged();
		return this;
	}

	public List<I> getItems() {
		return elements;
	}

	/**
	 * Udpate view to display the item. Default implementation calls
	 * {@link BaseViewHolder#bind(Object, int)}, subclasses may override.
	 */
	protected void update(int position, V view, I item) {
		view.bind(item, position);
	}

	/**
	 * Create empty item view
	 * 
	 * @param view
	 * @return item
	 */
	protected abstract V createView(View view);

	public final View getView(final int position, View convertView,
			final ViewGroup parent) {
		@SuppressWarnings("unchecked")
		V view = convertView != null ? (V) convertView.getTag() : null;
		if (view == null) {
			convertView = inflater.inflate(viewId, null);
			view = createView(convertView);
			convertView.setTag(view);
		}
		update(position, view, getItem(position));
		return convertView;
	}
}