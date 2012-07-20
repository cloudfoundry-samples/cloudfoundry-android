package org.cloudfoundry.android.cfdroid.support.masterdetail;

import java.util.List;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.rtyley.android.sherlock.roboguice.fragment.RoboSherlockFragment;

/**
 * Helper class for a detail fragment, dealing with restoring state upon various
 * events. Exposes actual selected data to subclasses via
 * {@link #getSelectedItem()} and {@link #getSelectedPosition()}.
 * 
 * @author ebottard
 * 
 * @param <I> the kind of domain object we're dealing with
 */
public abstract class AbstractDetailPane<I> extends RoboSherlockFragment
		implements DetailPaneEventsCallback {

	private int position = -1;

	@Override
	public final View onCreateView(LayoutInflater inflater,
			ViewGroup container, Bundle savedInstanceState) {

		// Were we launched in single pane mode ?
		Bundle args = getArguments();
		if (args != null && args.containsKey(KEY_SELECTION)) {
			position = args.getInt(DetailPaneEventsCallback.KEY_SELECTION);
		}

		// Are we recreated after eg a config change ?
		if (savedInstanceState != null
				&& savedInstanceState
						.containsKey(DetailPaneEventsCallback.KEY_SELECTION)) {
			position = savedInstanceState
					.getInt(DetailPaneEventsCallback.KEY_SELECTION);
		}

		View v = onCreateViewInternal(inflater, container, savedInstanceState);
		return v;
	}

	protected abstract View onCreateViewInternal(LayoutInflater inflater,
			ViewGroup container, Bundle savedInstanceState);

	protected int getSelectedPosition() {
		return position;
	}

	@SuppressWarnings("unchecked")
	protected I getSelectedItem() {
		if (position > -1) {
			List<I> data = ((DataHolder<I>) getActivity()).getData();
			return data != null ? data.get(position) : null;
		} else {
			return null;
		}
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		if (savedInstanceState != null) {
			position = savedInstanceState
					.getInt(DetailPaneEventsCallback.KEY_SELECTION);
		}
		// Fire an initial selection change, so that
		// subclasses may update their views.
		itemSelected(position);

	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt(DetailPaneEventsCallback.KEY_SELECTION, position);
	}

	@Override
	public final void itemSelected(int position) {
		this.position = position;
		itemSelectedInternal(position);
	}

	protected void itemSelectedInternal(int position) {

	}

}
