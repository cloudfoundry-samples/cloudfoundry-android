package org.cloudfoundry.android.cfdroid;

import java.util.List;

import org.cloudfoundry.client.lib.CloudInfo;
import org.cloudfoundry.client.lib.CloudService;

import com.googlecode.androidannotations.annotations.Background;
import com.googlecode.androidannotations.annotations.Bean;
import com.googlecode.androidannotations.annotations.EBean;
import com.googlecode.androidannotations.annotations.RootContext;

@EBean
public class CloudInfoDLTask {

	@RootContext
	CloudInfoActivity activity;
	
	@Bean
	Clients clients;
	

	private volatile boolean dlInProgress;
	
	@Background
	void dl() {
		dlInProgress = true;
		activity.showDLStatus(); 
		
		CloudInfo info = clients.getCloudInfo();
		
		dlInProgress = false;
		activity.showDLStatus();
		
		activity.updateData(info);
	}
	
	public boolean isDlInProgress() {
		return dlInProgress;
	}
}
