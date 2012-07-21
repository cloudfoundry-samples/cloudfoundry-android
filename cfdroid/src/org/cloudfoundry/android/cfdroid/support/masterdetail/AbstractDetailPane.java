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

	@Override
	public final View onCreateView(LayoutInflater inflater,
			ViewGroup container, Bundle savedInstanceState) {
		View v = onCreateViewInternal(inflater, container, savedInstanceState);
		return v;
	}

	protected abstract View onCreateViewInternal(LayoutInflater inflater,
			ViewGroup container, Bundle savedInstanceState);

	protected I getSelectedItem() {
		return ((DataHolder<I>)getActivity()).getSelectedItem();
	}
	
}
