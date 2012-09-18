package org.cloudfoundry.android.cfdroid.applications;

import java.util.Collections;
import java.util.List;

import org.cloudfoundry.android.cfdroid.CloudFoundry;
import org.cloudfoundry.android.cfdroid.support.AsyncLoader;
import org.cloudfoundry.android.cfdroid.support.ListLoadingFragment;
import org.cloudfoundry.client.lib.CloudApplication;

import android.app.Activity;
import android.database.ContentObserver;

public class ApplicationsListLoader extends AsyncLoader<List<CloudApplication>> {

	private CloudFoundry client;
	private ContentObserver observer = this.new ForceLoadContentObserver();
	private boolean force = true;

	public ApplicationsListLoader(Activity activity, CloudFoundry client) {
		super(activity.getApplicationContext());
		this.client = client;
	}

	@Override
	protected void onAbandon() {
		client.stopListeningForApplicationUpdates(observer);
		super.onAbandon();
	}

	@Override
	public void onContentChanged() {
		forceLoad();
	}

	@Override
	public List<CloudApplication> loadInBackground() {
		try {
			List<CloudApplication> applications = client.getApplications(force);
			if (force) {
				force = false;
				client.listenForApplicationsUpdates(observer);
			}
			return applications;
		} catch (Exception e) {
			return ListLoadingFragment.ERROR;
		}
	}

}
