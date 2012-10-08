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
package org.cloudfoundry.android.cfdroid;

import org.cloudfoundry.android.cfdroid.applications.ApplicationsActivity;
import org.cloudfoundry.android.cfdroid.cloud.CloudInfoActivity;
import org.cloudfoundry.android.cfdroid.services.ServicesActivity;

import roboguice.inject.ContentView;
import roboguice.inject.InjectView;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.github.rtyley.android.sherlock.roboguice.activity.RoboSherlockActivity;

/**
 * Entry point for our application. Shows four buttons that launch other
 * activities.
 * 
 * @author Eric Bottard
 * 
 */
@ContentView(R.layout.dashboard)
public class DashboardActivity extends RoboSherlockActivity {

	@InjectView(R.id.db_apps_btn)
	private View apps;

	@InjectView(R.id.db_services_btn)
	private View services;

	@InjectView(R.id.db_cloud_btn)
	private View cloud;

	@InjectView(R.id.db_about_btn)
	private View about;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		apps.setOnClickListener(launch(ApplicationsActivity.class));
		services.setOnClickListener(launch(ServicesActivity.class));
		cloud.setOnClickListener(launch(CloudInfoActivity.class));
	}

	private OnClickListener launch(final Class<? extends Activity> klass) {
		return new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(DashboardActivity.this, klass);
				startActivity(intent);

			}
		};
	}

}
