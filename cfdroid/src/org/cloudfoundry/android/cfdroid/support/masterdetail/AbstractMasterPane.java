package org.cloudfoundry.android.cfdroid.support.masterdetail;

import org.cloudfoundry.android.cfdroid.support.ListLoadingFragment;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

/**
 * Helper class for a master fragment that retrieves data through a loader.
 * 
 * @author ebottard
 *
 * @param <I> the kind of domain object this fragment deals with
 */
public abstract class AbstractMasterPane<I> extends ListLoadingFragment<I> {
	
	private int position;
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt(DetailPaneEventsCallback.KEY_SELECTION, position);
	};
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		if (savedInstanceState != null && savedInstanceState.containsKey(DetailPaneEventsCallback.KEY_SELECTION)) {
			position = savedInstanceState.getInt(DetailPaneEventsCallback.KEY_SELECTION);
		}
	}
	
	@Override
	public void onResume() {
		super.onResume();
		if (position > -1) {
			communicationChannel().onLeftPaneSelection(position);
		}
	}
	
	protected MasterDetailEventsCallback<I> communicationChannel() {
		return (MasterDetailEventsCallback<I>) getActivity();
	}
	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		communicationChannel()
				.onLeftPaneSelection(position);
		getListView().setItemChecked(position, true);
	}
	
	@Override
	public void onStart() {
		super.onStart();
		// TODO: two pane only, but dot not seem to work
		getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
	}



}
