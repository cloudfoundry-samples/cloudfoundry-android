package org.cloudfoundry.android.cfdroid.applications;

import java.util.List;

import org.cloudfoundry.android.cfdroid.CloudFoundry;
import org.cloudfoundry.android.cfdroid.support.AsyncLoader;
import org.cloudfoundry.client.lib.CloudApplication;

import android.app.Activity;

public class ApplicationsListLoader extends AsyncLoader<List<CloudApplication>>{

	private CloudFoundry client;
	
	public ApplicationsListLoader(Activity activity, CloudFoundry client) {
		super(activity);
		this.client = client;
	}

	@Override
	public List<CloudApplication> loadInBackground() {
		return client.getApplications();
	}

}
