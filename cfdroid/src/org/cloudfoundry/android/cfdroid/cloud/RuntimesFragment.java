package org.cloudfoundry.android.cfdroid.cloud;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.inject.Inject;

import org.cloudfoundry.android.cfdroid.CloudFoundry;
import org.cloudfoundry.android.cfdroid.R;
import org.cloudfoundry.android.cfdroid.support.AsyncLoader;
import org.cloudfoundry.android.cfdroid.support.ItemListAdapter;
import org.cloudfoundry.android.cfdroid.support.ListLoadingFragment;
import org.cloudfoundry.client.lib.CloudInfo.Runtime;

import android.os.Bundle;
import android.support.v4.content.Loader;
import android.view.View;

public class RuntimesFragment extends
		ListLoadingFragment<org.cloudfoundry.client.lib.CloudInfo.Runtime> {

	@Inject
	private CloudFoundry client;

	@Override
	public Loader<List<Runtime>> onCreateLoader(int id, Bundle args) {
		return new AsyncLoader<List<Runtime>>(getActivity()) {
			@Override
			public List<Runtime> loadInBackground() {
				ArrayList<Runtime> runtimes = new ArrayList<Runtime>(client.getCloudInfo()
						.getRuntimes());
				Collections.sort(runtimes, new Comparator<Runtime>() {
					@Override
					public int compare(Runtime lhs, Runtime rhs) {
						return lhs.getName().compareTo(rhs.getName());
					}
				});
				return runtimes;
			}

		};
	}

	@Override
	protected ItemListAdapter<Runtime, ?> adapterFor(List<Runtime> items) {
		return new ItemListAdapter<org.cloudfoundry.client.lib.CloudInfo.Runtime, RuntimeView>(
				R.layout.runtime_list_item, getActivity().getLayoutInflater(),
				items) {

			@Override
			protected RuntimeView createView(View view) {
				return new RuntimeView(view);
			}
		};
	}

}
