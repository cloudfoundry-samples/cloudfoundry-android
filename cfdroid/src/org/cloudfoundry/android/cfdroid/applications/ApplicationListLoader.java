package org.cloudfoundry.android.cfdroid.applications;

import java.util.List;

import org.cloudfoundry.android.cfdroid.Clients;
import org.cloudfoundry.android.cfdroid.support.AsyncLoader;
import org.cloudfoundry.client.lib.CloudApplication;

import android.app.Activity;

import com.google.inject.Inject;

public class ApplicationListLoader extends AsyncLoader<List<CloudApplication>>{

	private Clients clients;
	
	@Inject
	public ApplicationListLoader(Activity activity, Clients clients) {
		super(activity);
		this.clients = clients;
	}

	@Override
	public List<CloudApplication> loadInBackground() {
		return clients.getApplications();
	}

}
