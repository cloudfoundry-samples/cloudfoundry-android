package org.cloudfoundry.android.cfdroid.services;

import java.util.List;

import org.cloudfoundry.android.cfdroid.CloudFoundry;
import org.cloudfoundry.android.cfdroid.support.FailingAsyncLoader;
import org.cloudfoundry.client.lib.CloudService;

import android.app.Activity;
import android.database.ContentObserver;

public class ServicesListLoader extends FailingAsyncLoader<List<CloudService>>{

	private CloudFoundry client;
	
	private ContentObserver contentObserver = this.new ForceLoadContentObserver();
	
	private boolean force = true;
	
	public ServicesListLoader(Activity activity, CloudFoundry client) {
		super(activity);
		this.client = client;
	}
	
	@Override
	protected void onAbandon() {
		client.stopListeningForServicesUpdates(contentObserver);
		super.onAbandon();
	}
	
	@Override
	public List<CloudService> doLoadInBackground() {
		List<CloudService> services = client.getServices(force);
		if (force) {
			force = false;
			client.listenForServicesUpdates(contentObserver);
		}
		return services;
	}
	

}
