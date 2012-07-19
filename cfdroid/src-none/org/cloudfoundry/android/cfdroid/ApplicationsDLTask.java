package org.cloudfoundry.android.cfdroid;

import java.util.List;

import org.cloudfoundry.client.lib.CloudApplication;

import com.googlecode.androidannotations.annotations.Background;
import com.googlecode.androidannotations.annotations.Bean;
import com.googlecode.androidannotations.annotations.EBean;
import com.googlecode.androidannotations.annotations.RootContext;

@EBean
public class ApplicationsDLTask {
	
	@RootContext
	AppListActivity activity;
	
	@Bean
	Clients clients;

	private volatile boolean dlInProgress;
	
	@Background
	void dl() {
		dlInProgress = true;
		activity.showDLStatus(); 
		
		List<CloudApplication> apps = clients.getApplications();
		
		dlInProgress = false;
		activity.showDLStatus();
		
		activity.updateData(apps);
	}
	
	public boolean isDlInProgress() {
		return dlInProgress;
	}
	
}
