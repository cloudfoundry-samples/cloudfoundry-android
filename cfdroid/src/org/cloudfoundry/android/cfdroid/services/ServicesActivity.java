package org.cloudfoundry.android.cfdroid.services;

import java.util.List;

import org.cloudfoundry.android.cfdroid.R;
import org.cloudfoundry.android.cfdroid.support.masterdetail.MasterDetailActivity;
import org.cloudfoundry.client.lib.CloudService;

import roboguice.inject.ContentView;

import android.os.Bundle;

/**
 * Master/Detail activity for services. Shows a list of services and details
 * about one on the next/right screen.
 * 
 * @author Eric Bottard
 * 
 */
@ContentView(R.layout.services)
public class ServicesActivity
		extends
		MasterDetailActivity<CloudService, ServicesListFragment, ServiceDetailFragment> {
	@Override
	public void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		getSupportActionBar().setDisplayShowTitleEnabled(false);
	}

	@Override
	public void onNewData(List<CloudService> data) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	protected int rightPaneLayout() {
		return R.layout.right_pane_services;
	}

}
