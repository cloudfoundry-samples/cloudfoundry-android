/*
 * Copyright 2012 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.cloudfoundry.android.cfdroid.applications;

import java.util.List;

import org.cloudfoundry.android.cfdroid.CloudFoundry;
import org.cloudfoundry.android.cfdroid.R;
import org.cloudfoundry.android.cfdroid.support.ItemListAdapter;
import org.cloudfoundry.android.cfdroid.support.ListLoadingFragment;
import org.cloudfoundry.android.cfdroid.support.Result;
import org.cloudfoundry.android.cfdroid.support.masterdetail.DetailPaneEventsCallback;
import org.cloudfoundry.client.lib.CloudApplication;
import org.cloudfoundry.client.lib.InstanceStats;

import android.os.Bundle;
import android.support.v4.content.Loader;
import android.view.View;
import android.widget.ListView;

import com.google.inject.Inject;
import com.udinic.expand_animation_example.ExpandAnimation;

/**
 * A fragment that displays info about server instance state.
 * 
 * @author Eric Bottard
 * 
 */
public class ApplicationInstanceStatsFragment extends
		ListLoadingFragment<InstanceStats> implements DetailPaneEventsCallback {

	@Inject
	private CloudFoundry client;

	private int position;

	@Override
	public Loader<Result<List<InstanceStats>>> onCreateLoader(int id,
			Bundle args) {
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

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		InstanceStats stats = (InstanceStats) l.getItemAtPosition(position);
		
		View details = v.findViewById(R.id.details);
		ExpandAnimation animation = new ExpandAnimation(details, 500);
		details.startAnimation(animation);
		
	}
}
