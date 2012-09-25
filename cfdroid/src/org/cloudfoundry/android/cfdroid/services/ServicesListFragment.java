package org.cloudfoundry.android.cfdroid.services;

import java.util.List;

import javax.inject.Inject;

import org.cloudfoundry.android.cfdroid.CloudFoundry;
import org.cloudfoundry.android.cfdroid.R;
import org.cloudfoundry.android.cfdroid.support.ItemListAdapter;
import org.cloudfoundry.android.cfdroid.support.Result;
import org.cloudfoundry.android.cfdroid.support.masterdetail.AbstractMasterPane;
import org.cloudfoundry.client.lib.CloudService;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.Loader;
import android.view.View;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;

/**
 * A fragment that shows the list of all available services. Also allows
 * provisionning a new service.
 * 
 * @author Eric Bottard
 * 
 */
public class ServicesListFragment extends AbstractMasterPane<CloudService> {

	@Inject
	private CloudFoundry client;

	@Override
	public Loader<Result<List<CloudService>>> onCreateLoader(int id, Bundle args) {
		return new ServicesListLoader(getActivity(), client);
	}

	@Override
	protected ItemListAdapter<CloudService, ?> adapterFor(
			List<CloudService> items) {
		return new ItemListAdapter<CloudService, ServiceView>(
				R.layout.service_list_item, getActivity().getLayoutInflater(),
				items) {

			@Override
			protected ServiceView createView(View view) {
				return new ServiceView(view);
			}
		};
	}

	@Override
	public void onCreateOptionsMenu(Menu optionsMenu, MenuInflater inflater) {
		super.onCreateOptionsMenu(optionsMenu, inflater);
		inflater.inflate(R.menu.add, optionsMenu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.add:
			provisionService();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void provisionService() {
		FragmentManager fm = getActivity().getSupportFragmentManager();
		ServiceEditDialogFragment dialog = new ServiceEditDialogFragment();
		dialog.show(fm, "fragment_edit_service");
		
	}

	@Override
	protected int loaderId() {
		return R.id.services_loader;
	}

}
