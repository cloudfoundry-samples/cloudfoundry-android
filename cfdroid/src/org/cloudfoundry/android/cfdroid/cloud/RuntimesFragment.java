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
import org.cloudfoundry.android.cfdroid.support.FailingAsyncLoader;
import org.cloudfoundry.android.cfdroid.support.ItemListAdapter;
import org.cloudfoundry.android.cfdroid.support.ListLoadingFragment;
import org.cloudfoundry.android.cfdroid.support.Result;
import org.cloudfoundry.client.lib.CloudInfo.Runtime;

import android.os.Bundle;
import android.support.v4.content.Loader;
import android.view.View;

public class RuntimesFragment extends
		ListLoadingFragment<org.cloudfoundry.client.lib.CloudInfo.Runtime> {

	@Inject
	private CloudFoundry client;

	@Override
	public Loader<Result<List<Runtime>>> onCreateLoader(int id, Bundle args) {
		return new FailingAsyncLoader<List<Runtime>>(getActivity()) {
			@Override
			public List<Runtime> doLoadInBackground() {
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

			@Override
			public boolean isEnabled(int position) {
				return false;
			}

		};
	}

	@Override
	protected int loaderId() {
		return R.id.runtimes_loader;
	}

}
