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

	protected MasterDetailEventsCallback communicationChannel() {
		return (MasterDetailEventsCallback) getActivity();
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		communicationChannel().onLeftPaneSelection(position);
	}

}
