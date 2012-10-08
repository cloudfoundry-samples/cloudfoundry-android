/*
 * Copyright 2012 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
	 * Create empty adapter.
	 */
	public ItemListAdapter(final int viewId, final LayoutInflater inflater) {
		this(viewId, inflater, new ArrayList<I>());
	}

	/**
	 * Create adapter with the given underlying data.
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
	 * Set underlying data.
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
	 * Create empty item view.
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