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
import org.cloudfoundry.android.cfdroid.support.FailingAsyncLoader;
import org.cloudfoundry.client.lib.InstanceStats;

import android.app.Activity;
import android.database.ContentObserver;

public class InstanceStatsLoader extends
		FailingAsyncLoader<List<InstanceStats>> {

	private CloudFoundry client;
	private ContentObserver observer = this.new ForceLoadContentObserver();
	private boolean firstTime = true;

	private String appName;

	public InstanceStatsLoader(Activity activity, CloudFoundry client,
			String appName) {
		super(activity);
		this.client = client;
		this.appName = appName;
	}

	@Override
	protected void onAbandon() {
		if (!firstTime) {
			client.stopListeningForApplicationUpdates(observer);
		}
		super.onAbandon();
	}

	@Override
	public List<InstanceStats> doLoadInBackground() {
		List<InstanceStats> applicationStats = client
				.getApplicationStats(appName);
		if (firstTime) {
			firstTime = false;
			client.listenForApplicationsUpdates(observer);
		}
		return applicationStats;
	}
}
