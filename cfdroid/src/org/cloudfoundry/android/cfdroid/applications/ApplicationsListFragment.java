package org.cloudfoundry.android.cfdroid.applications;

import java.util.List;

import org.cloudfoundry.android.cfdroid.Clients;
import org.cloudfoundry.android.cfdroid.R;
import org.cloudfoundry.android.cfdroid.support.ItemListAdapter;
import org.cloudfoundry.android.cfdroid.support.ListLoadingFragment;
import org.cloudfoundry.client.lib.CloudApplication;

import android.os.Bundle;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.inject.Inject;

public class ApplicationsListFragment extends
		ListLoadingFragment<CloudApplication> {

	@Inject
	private Clients clients;

	public static interface OnApplicationSelectedListener {
		void onApplicationSelected(int position);
	}

	@Override
	public Loader<List<CloudApplication>> onCreateLoader(int arg0, Bundle arg1) {
		return new ApplicationListLoader(getActivity(), clients);
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		((OnApplicationSelectedListener)getActivity()).onApplicationSelected(position);
	}
	
	
	@Override
	protected ItemListAdapter<CloudApplication, ?> adapterFor(
			List<CloudApplication> items) {
		return new ItemListAdapter<CloudApplication, ApplicationView>(
				R.layout.application_list_item, getActivity()
						.getLayoutInflater(), items) {

							@Override
							protected void update(int position,
									ApplicationView view, CloudApplication item) {
								view.name.setText(item.getName());
							}

							@Override
							protected ApplicationView createView(View view) {
								return new ApplicationView(view);
							}
		};
	}
}
