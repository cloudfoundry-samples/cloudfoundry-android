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

import java.util.EnumMap;

import org.cloudfoundry.android.cfdroid.R;
import org.cloudfoundry.android.cfdroid.cloud.FrameworkLogos;
import org.cloudfoundry.android.cfdroid.services.ServiceLogos;
import org.cloudfoundry.android.cfdroid.support.BaseViewHolder;
import org.cloudfoundry.client.lib.CloudApplication;
import org.cloudfoundry.client.lib.CloudApplication.AppState;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class ApplicationView extends BaseViewHolder<CloudApplication> {

	private TextView name;
	private TextView status;
	private ImageView logo;

	/*default*/ final static EnumMap<AppState, Integer> COLORS = new EnumMap<CloudApplication.AppState, Integer>(
			AppState.class);
	static {
		COLORS.put(AppState.STARTED, R.color.status_started);
		COLORS.put(AppState.STOPPED, R.color.status_stopped);
		COLORS.put(AppState.UPDATING, R.color.status_updating);
	}

	public ApplicationView(View container) {
		name = (TextView) container.findViewById(R.id.name);
		status = (TextView) container.findViewById(R.id.status);
		logo = (ImageView) container.findViewById(R.id.logo);
	}

	@Override
	public void bind(CloudApplication application) {
		name.setText(application.getName());
		AppState state = application.getState();
		status.setText("" + state);
		status.setTextColor(status.getResources().getColor(COLORS.get(state)));
		
		FrameworkLogos logoEnum;
		try {
			String fwk = (String) application.getStaging().get("model");
			logoEnum = FrameworkLogos.valueOf(fwk);
		} catch (IllegalArgumentException notFound) {
			logoEnum = FrameworkLogos.unknown;
		}
		logo.setImageLevel(logoEnum.level);

	}
}
