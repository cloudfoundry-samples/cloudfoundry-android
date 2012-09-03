package org.cloudfoundry.android.cfdroid.services;

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
	protected ServicesListFragment makeLeftFragment() {
		return new ServicesListFragment();
	}

	@Override
	protected ServiceDetailFragment makeRightFragment() {
		return new ServiceDetailFragment();
	}

}
