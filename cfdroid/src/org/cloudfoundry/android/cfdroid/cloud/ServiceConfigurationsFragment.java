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
package org.cloudfoundry.android.cfdroid.cloud;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.inject.Inject;

import org.cloudfoundry.android.cfdroid.CloudFoundry;
import org.cloudfoundry.android.cfdroid.R;
import org.cloudfoundry.android.cfdroid.services.ServiceConfigurationView;
import org.cloudfoundry.android.cfdroid.support.FailingAsyncLoader;
import org.cloudfoundry.android.cfdroid.support.ItemListAdapter;
import org.cloudfoundry.android.cfdroid.support.ListLoadingFragment;
import org.cloudfoundry.android.cfdroid.support.Result;
import org.cloudfoundry.client.lib.ServiceConfiguration;

import android.os.Bundle;
import android.support.v4.content.Loader;
import android.view.View;

public class ServiceConfigurationsFragment extends
		ListLoadingFragment<ServiceConfiguration> {

	@Inject
	private CloudFoundry client;

	@Override
	public Loader<Result<List<ServiceConfiguration>>> onCreateLoader(int id,
			Bundle args) {
		return new FailingAsyncLoader<List<ServiceConfiguration>>(getActivity()) {
			@Override
			public List<ServiceConfiguration> doLoadInBackground() {
				ArrayList<ServiceConfiguration> configurations = new ArrayList<ServiceConfiguration>(
						client.getServiceConfigurations());
				Collections.sort(configurations,
						new Comparator<ServiceConfiguration>() {
							@Override
							public int compare(ServiceConfiguration lhs,
									ServiceConfiguration rhs) {
								return lhs.getVendor().compareTo(
										rhs.getVendor());
							}
						});
				return configurations;
			}

		};
	}

	@Override
	protected int loaderId() {
		return R.id.service_configurations_loader;
	}

	@Override
	protected ItemListAdapter<ServiceConfiguration, ?> adapterFor(
			List<ServiceConfiguration> items) {
		return new ItemListAdapter<ServiceConfiguration, ServiceConfigurationView>(
				R.layout.service_config_list_item, getActivity().getLayoutInflater(),
				items) {
			@Override
			protected ServiceConfigurationView createView(View view) {
				return new ServiceConfigurationView(view);
			}

			@Override
			public boolean isEnabled(int position) {
				return false;
			}

		};
	}
}
