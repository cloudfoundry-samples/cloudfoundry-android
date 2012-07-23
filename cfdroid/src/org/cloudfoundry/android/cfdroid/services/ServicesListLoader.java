package org.cloudfoundry.android.cfdroid.services;

import java.util.List;

import org.cloudfoundry.android.cfdroid.Clients;
import org.cloudfoundry.android.cfdroid.support.AsyncLoader;
import org.cloudfoundry.client.lib.CloudService;

import android.app.Activity;

import com.google.inject.Inject;

public class ServicesListLoader extends AsyncLoader<List<CloudService>>{

	private Clients clients;
	
	@Inject
	public ServicesListLoader(Activity activity, Clients clients) {
		super(activity);
		this.clients = clients;
	}
	
	@Override
	public List<CloudService> loadInBackground() {
		return clients.getServices();
	}

}
