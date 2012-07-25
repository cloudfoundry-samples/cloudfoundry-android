package org.cloudfoundry.android.cfdroid.services;

import java.util.List;

import org.cloudfoundry.android.cfdroid.CloudFoundry;
import org.cloudfoundry.android.cfdroid.support.AsyncLoader;
import org.cloudfoundry.client.lib.CloudService;

import android.app.Activity;

public class ServicesListLoader extends AsyncLoader<List<CloudService>>{

	private CloudFoundry clients;
	
	public ServicesListLoader(Activity activity, CloudFoundry clients) {
		super(activity);
		this.clients = clients;
	}
	
	@Override
	public List<CloudService> loadInBackground() {
		return clients.getServices();
	}

}
