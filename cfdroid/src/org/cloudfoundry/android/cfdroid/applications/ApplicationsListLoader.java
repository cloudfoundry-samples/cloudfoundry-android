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
import org.cloudfoundry.client.lib.CloudApplication;

import android.app.Activity;
import android.database.ContentObserver;

public class ApplicationsListLoader extends
		FailingAsyncLoader<List<CloudApplication>> {

	private CloudFoundry client;
	private ContentObserver observer = this.new ForceLoadContentObserver();
	private boolean force = true;

	public ApplicationsListLoader(Activity activity, CloudFoundry client) {
		super(activity.getApplicationContext());
		this.client = client;
	}

	@Override
	protected void onAbandon() {
		if (!force) {
			client.stopListeningForApplicationUpdates(observer);
		}
		super.onAbandon();
	}

	@Override
	public void onContentChanged() {
		forceLoad();
	}

	@Override
	public List<CloudApplication> doLoadInBackground() {
		List<CloudApplication> applications = client.getApplications(force);
		if (force) {
			force = false;
			client.listenForApplicationsUpdates(observer);
		}
		return applications;
	}

}
