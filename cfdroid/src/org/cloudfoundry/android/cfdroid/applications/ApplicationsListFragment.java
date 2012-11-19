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
import org.cloudfoundry.android.cfdroid.support.Result;
import org.cloudfoundry.android.cfdroid.support.masterdetail.AbstractMasterPane;
import org.cloudfoundry.client.lib.CloudApplication;

import roboguice.inject.InjectView;
import android.os.Bundle;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.inject.Inject;

public class ApplicationsListFragment extends
		AbstractMasterPane<CloudApplication> {

	@Inject
	private CloudFoundry client;

	@InjectView(R.id.apps_header)
	private TextView header;

	@Override
	public Loader<Result<List<CloudApplication>>> onCreateLoader(int arg0,
			Bundle arg1) {
		return new ApplicationsListLoader(getActivity(), client);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		LinearLayout outer = (LinearLayout) inflater.inflate(
				R.layout.left_pane_applications_list, container);

		// Contrary to what ListFragment#onCreateView() currently
		// states, just including the standard layout does not work. So
		// we manually call to super() and add it to our container.
		View v = super.onCreateView(inflater, null, savedInstanceState);
		outer.addView(v);
		return outer;
	}

	@Override
	protected void setList(List<CloudApplication> items) {
		super.setList(items);
		int count = items.size();
		header.setText(getActivity().getResources().getQuantityString(
				R.plurals.number_of_apps, count, count));
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

	@Override
	protected int loaderId() {
		return R.id.application_list_loader;
	}
}
