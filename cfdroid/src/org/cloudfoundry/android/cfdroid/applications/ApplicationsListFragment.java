package org.cloudfoundry.android.cfdroid.applications;

import java.util.List;

import org.cloudfoundry.android.cfdroid.Clients;
import org.cloudfoundry.android.cfdroid.R;
import org.cloudfoundry.android.cfdroid.support.ItemListAdapter;
import org.cloudfoundry.android.cfdroid.support.ListLoadingFragment;
import org.cloudfoundry.client.lib.CloudApplication;

import android.os.Bundle;
import android.support.v4.content.Loader;
import android.view.View;
import android.widget.ListView;

import com.google.inject.Inject;

public class ApplicationsListFragment extends
		ListLoadingFragment<CloudApplication> {

	@Inject
	private Clients clients;

	public static interface Listener {
		void onApplicationSelected(int position);
		
		void onApplicationsLoaded(List<CloudApplication> applications);
		
		CloudApplication getApplication(int position);
	}

	@Override
	public Loader<List<CloudApplication>> onCreateLoader(int arg0, Bundle arg1) {
		return new ApplicationsListLoader(getActivity(), clients);
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		getListener()
				.onApplicationSelected(position);
		getListView().setItemChecked(position, true);
	}
	
	@Override
	public void onStart() {
		super.onStart();
		getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
	}
	
	@Override
	public void onLoadFinished(Loader<List<CloudApplication>> loader,
			List<CloudApplication> items) {
		super.onLoadFinished(loader, items);
		getListener().onApplicationsLoaded(items);
	}
	
	private Listener getListener() {
		return (Listener) getActivity();
	}
	
	
	@Override
	protected ItemListAdapter<CloudApplication, ?> adapterFor(
			List<CloudApplication> items) {
		return new ItemListAdapter<CloudApplication, ApplicationView>(
				R.layout.application_list_item, getActivity()
						.getLayoutInflater(), items) {
			@Override
			protected ApplicationView createView(View view) {
				return new ApplicationView(view);
			}
		};
	}
}
