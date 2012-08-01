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
import org.cloudfoundry.client.lib.CloudInfo.Framework;

import android.os.Bundle;
import android.support.v4.content.Loader;
import android.view.View;

public class FrameworksFragment extends
		ListLoadingFragment<Framework> {

	@Inject
	private CloudFoundry client;

	@Override
	public Loader<List<Framework>> onCreateLoader(int id, Bundle args) {
		return new AsyncLoader<List<Framework>>(getActivity()) {
			@Override
			public List<Framework> loadInBackground() {
				ArrayList<Framework> frameworks = new ArrayList<Framework>(client.getCloudInfo()
						.getFrameworks());
				Collections.sort(frameworks, new Comparator<Framework>() {
					@Override
					public int compare(Framework lhs, Framework rhs) {
						return lhs.getName().compareTo(rhs.getName());
					}
				});
				return frameworks;
			}

		};
	}

	@Override
	protected ItemListAdapter<Framework, ?> adapterFor(List<Framework> items) {
		return new ItemListAdapter<Framework, FrameworkView>(
				R.layout.framework_list_item, getActivity().getLayoutInflater(),
				items) {

			@Override
			protected FrameworkView createView(View view) {
				return new FrameworkView(view);
			}
		};
	}

}
