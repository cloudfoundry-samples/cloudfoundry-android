package org.cloudfoundry.android.cfdroid.support.masterdetail;

import org.cloudfoundry.android.cfdroid.support.ListLoadingFragment;

import android.view.View;
import android.widget.ListView;

/**
 * Helper class for a master fragment that retrieves data through a loader.
 * 
 * @param <I>
 *            the kind of domain object this fragment deals with
 * @author Eric Bottard
 */
public abstract class AbstractMasterPane<I> extends ListLoadingFragment<I> {

	protected MasterDetailEventsCallback<I> communicationChannel() {
		return (MasterDetailEventsCallback<I>) getActivity();
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		communicationChannel().onLeftPaneSelection(position);
		getListView().setItemChecked(position, true);
	}

	@Override
	public void onStart() {
		super.onStart();
		// TODO: two pane only, but dot not seem to work
		getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
	}

}
