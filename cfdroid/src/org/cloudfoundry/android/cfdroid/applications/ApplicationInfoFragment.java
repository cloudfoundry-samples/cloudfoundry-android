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

import javax.inject.Inject;

import org.cloudfoundry.android.cfdroid.CloudFoundry;
import org.cloudfoundry.android.cfdroid.R;
import org.cloudfoundry.android.cfdroid.support.BaseViewHolder;
import org.cloudfoundry.android.cfdroid.support.ItemListAdapter;
import org.cloudfoundry.android.cfdroid.support.masterdetail.DetailPaneEventsCallback;
import org.cloudfoundry.client.lib.CloudApplication;

import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.TextView;

import com.github.rtyley.android.sherlock.roboguice.fragment.RoboSherlockListFragment;

public class ApplicationInfoFragment extends RoboSherlockListFragment implements
		DetailPaneEventsCallback {

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		List<String> urls = getCloudApplication().getUris();
		setListAdapter(new ItemListAdapter<String, ApplicationLinkView>(
				R.layout.application_url_item, getActivity()
						.getLayoutInflater(), urls) {

			@Override
			protected ApplicationLinkView createView(View view) {
				return new ApplicationLinkView(view);
			}
		});
	}

	@Inject
	private CloudFoundry client;

	private int position;

	@Override
	public void selectionChanged(int position) {
		this.position = position;
	}

	private CloudApplication getCloudApplication() {
		return client.getApplications(false).get(position);
	}

	/* default */static class ApplicationLinkView extends
			BaseViewHolder<String> {
		private TextView text;

		private ApplicationLinkView(View v) {
			text = (TextView) v;
			text.setMovementMethod(LinkMovementMethod.getInstance());
		}

		@Override
		public void bind(String item) {
			text.setText("http://" + item);
		}
	}

}
