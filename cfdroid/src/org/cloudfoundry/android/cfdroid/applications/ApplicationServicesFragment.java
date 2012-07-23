package org.cloudfoundry.android.cfdroid.applications;

import java.util.List;

import org.cloudfoundry.android.cfdroid.Clients;
import org.cloudfoundry.android.cfdroid.R;
import org.cloudfoundry.android.cfdroid.services.ServicesListLoader;
import org.cloudfoundry.android.cfdroid.support.ItemListAdapter;
import org.cloudfoundry.android.cfdroid.support.ListLoadingFragment;
import org.cloudfoundry.android.cfdroid.support.masterdetail.DataHolder;
import org.cloudfoundry.client.lib.CloudApplication;
import org.cloudfoundry.client.lib.CloudService;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.google.inject.Inject;

import android.os.Bundle;
import android.support.v4.content.Loader;
import android.view.View;

/**
 * A fragment that shows all services in the current cloud, with information
 * about which service is bound to the current app and ability to bind/unbind
 * them.
 * 
 * @author ebottard
 * 
 */
public class ApplicationServicesFragment extends
		ListLoadingFragment<CloudService> {

	@Inject
	private Clients clients;

	@Override
	public Loader<List<CloudService>> onCreateLoader(int arg0, Bundle arg1) {
		return new ServicesListLoader(getActivity(), clients);
	}

	@Override
	protected ItemListAdapter<CloudService, ?> adapterFor(
			List<CloudService> items) {

		final CloudApplication app = ((DataHolder<CloudApplication>) getActivity())
				.getSelectedItem();

		return new ItemListAdapter<CloudService, BindableServiceView>(
				R.layout.application_service_list_item, getActivity()
						.getLayoutInflater(), items) {

			@Override
			protected BindableServiceView createView(View view) {
				return new BindableServiceView(view, app);
			}
		};
	}

	/*
	 * Don't create an options menu (in particular not one with refresh from
	 * superclass, which confuses user since left pane already has refresh).
	 */
	@Override
	public void onCreateOptionsMenu(Menu optionsMenu, MenuInflater inflater) {
	}

}
