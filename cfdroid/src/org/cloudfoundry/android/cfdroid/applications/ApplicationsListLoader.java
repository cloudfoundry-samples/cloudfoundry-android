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
