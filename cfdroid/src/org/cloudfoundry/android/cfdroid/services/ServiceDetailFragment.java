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
package org.cloudfoundry.android.cfdroid.services;

import java.util.List;

import org.cloudfoundry.android.cfdroid.CloudFoundry;
import org.cloudfoundry.android.cfdroid.R;
import org.cloudfoundry.android.cfdroid.applications.ApplicationsListLoader;
import org.cloudfoundry.android.cfdroid.cloud.FrameworkLogos;
import org.cloudfoundry.android.cfdroid.support.BaseViewHolder;
import org.cloudfoundry.android.cfdroid.support.ItemListAdapter;
import org.cloudfoundry.android.cfdroid.support.ListLoadingFragment;
import org.cloudfoundry.android.cfdroid.support.Result;
import org.cloudfoundry.android.cfdroid.support.TaskWithDialog;
import org.cloudfoundry.android.cfdroid.support.masterdetail.DetailPaneEventsCallback;
import org.cloudfoundry.client.lib.CloudApplication;
import org.cloudfoundry.client.lib.CloudService;

import android.database.ContentObserver;
import android.os.Bundle;
import android.support.v4.content.Loader;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.inject.Inject;

public class ServiceDetailFragment extends
		ListLoadingFragment<CloudApplication> implements
		DetailPaneEventsCallback {
	/**
	 * A view that shows info about an app as well as toggle button to
	 * bind/unbind the app to a given service.
	 * 
	 * @author Eric Bottard
	 */
	private class BindableApplicationView extends
			BaseViewHolder<CloudApplication> {

		private ImageView fwkLogo;
		private TextView name;
		private ImageView bindButton;

		public BindableApplicationView(View container) {
			fwkLogo = (ImageView) container.findViewById(R.id.logo);
			name = (TextView) container.findViewById(R.id.name);
			bindButton = (ImageView) container.findViewById(R.id.link_unlink);
		}

		@Override
		public void bind(final CloudApplication app) {

			name.setText(app.getName());
			String fwk = app.getStaging().get("model");
			FrameworkLogos logo = FrameworkLogos.unknown;

			try {
				logo = FrameworkLogos.valueOf(fwk);
			} catch (IllegalArgumentException ignore) {

			}
			fwkLogo.setImageLevel(logo.level);

			final boolean bound = app.getServices().contains(
					getCloudService().getName());
			bindButton.setImageLevel(bound ? 0 : 1);

			bindButton.setOnLongClickListener(new View.OnLongClickListener() {
				@Override
				public boolean onLongClick(View v) {
					toggleApplication(app.getName(), bound);
					return true;
				}

			});
		}

	}

	private int position;

	@Inject
	private CloudFoundry client;

	private ContentObserver contentObserver = new ContentObserver(null) {
		@Override
		public void onChange(boolean selfChange) {
			getActivity().runOnUiThread(new Runnable() {
				@Override
				public void run() {
					refresh();
				}
			});
		}
	};

	@Override
	protected ItemListAdapter<CloudApplication, ?> adapterFor(
			List<CloudApplication> items) {

		return new ItemListAdapter<CloudApplication, BindableApplicationView>(
				R.layout.service_application_list_item, getActivity()
						.getLayoutInflater(), items) {

			@Override
			protected BindableApplicationView createView(View view) {
				return ServiceDetailFragment.this.new BindableApplicationView(
						view);
			}
		};
	}

	private CloudService getCloudService() {
		return client.getServices(false).get(position);
	}

	@Override
	public Loader<Result<List<CloudApplication>>> onCreateLoader(int arg0,
			Bundle arg1) {
		return new ApplicationsListLoader(getActivity(), client);
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt("position", position);
	}

	public void onStart() {
		super.onStart();
		client.listenForServicesUpdates(contentObserver);
	}

	@Override
	public void onStop() {
		client.stopListeningForServicesUpdates(contentObserver);
		super.onStop();
	}

	public void selectionChanged(int position) {
		this.position = position;
		if (isResumed()) {
			refresh();
		}
	}

	private void toggleApplication(final String appName, final boolean unbind) {
		new TaskWithDialog<CloudApplication>(getActivity(), R.string.working) {
			@Override
			public CloudApplication call() throws Exception {
				if (unbind) {
					return client.unbindService(appName, getCloudService()
							.getName());
				} else {
					return client.bindService(appName, getCloudService()
							.getName());
				}
			}

			protected void onSuccess(CloudApplication t) throws Exception {
				ServiceDetailFragment.this.refresh();
			}

		}.execute();

	}

	@Override
	protected int loaderId() {
		return R.id.service_applications_loader;
	}
}