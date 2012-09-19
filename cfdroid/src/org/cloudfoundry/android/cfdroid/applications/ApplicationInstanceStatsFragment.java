package org.cloudfoundry.android.cfdroid.applications;

import java.util.List;

import org.cloudfoundry.android.cfdroid.CloudFoundry;
import org.cloudfoundry.android.cfdroid.R;
import org.cloudfoundry.android.cfdroid.support.ItemListAdapter;
import org.cloudfoundry.android.cfdroid.support.ListLoadingFragment;
import org.cloudfoundry.android.cfdroid.support.masterdetail.DetailPaneEventsCallback;
import org.cloudfoundry.client.lib.CloudApplication;
import org.cloudfoundry.client.lib.InstanceStats;

import roboguice.inject.InjectView;
import android.os.Bundle;
import android.support.v4.content.Loader;
import android.view.View;
import android.widget.ListView;

import com.google.inject.Inject;

public class ApplicationInstanceStatsFragment extends
		ListLoadingFragment<InstanceStats> implements DetailPaneEventsCallback {

	@Inject
	private CloudFoundry client;

	@InjectView(android.R.id.list)
	private ListView listView;

	private int position;
	
	@Override
	public Loader<List<InstanceStats>> onCreateLoader(int id, Bundle args) {
		return new InstanceStatsLoader(getActivity(), client,
				getCloudApplication().getName());
	}

	public void selectionChanged(int position) {
		this.position = position;
		if (isResumed()) {
			refresh();
		}
	}

	private CloudApplication getCloudApplication() {
		return client.getApplications(false).get(position); 
	}

	@Override
	protected ItemListAdapter<InstanceStats, ?> adapterFor(
			List<InstanceStats> items) {
		return new ItemListAdapter<InstanceStats, InstanceStatsView>(
				R.layout.application_instance_stat_item, getActivity()
						.getLayoutInflater(), items) {
			@Override
			protected InstanceStatsView createView(View view) {
				return new InstanceStatsView(view, R.string.not_available_abbr);
			}
		};
	}

	@Override
	protected int loaderId() {
		return R.id.application_stats_loader;
	}

}
