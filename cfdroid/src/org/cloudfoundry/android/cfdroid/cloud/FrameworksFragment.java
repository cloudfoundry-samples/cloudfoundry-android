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
import org.cloudfoundry.client.lib.CloudInfo.Framework;

import android.os.Bundle;
import android.support.v4.content.Loader;
import android.view.View;

public class FrameworksFragment extends
		ListLoadingFragment<Framework> {

	@Inject
	private CloudFoundry client;

	@Override
	public Loader<Result<List<Framework>>> onCreateLoader(int id, Bundle args) {
		return new FailingAsyncLoader<List<Framework>>(getActivity()) {
			@Override
			public List<Framework> doLoadInBackground() {
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

	@Override
	protected int loaderId() {
		return R.id.frameworks_loader;
	}

}
