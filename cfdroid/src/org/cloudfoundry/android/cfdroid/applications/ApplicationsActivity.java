package org.cloudfoundry.android.cfdroid.applications;

import org.cloudfoundry.android.cfdroid.R;
import org.cloudfoundry.android.cfdroid.support.masterdetail.MasterDetailActivity;
import org.cloudfoundry.client.lib.CloudApplication;

import roboguice.inject.ContentView;
import android.os.Bundle;

@ContentView(R.layout.applications)
public class ApplicationsActivity
		extends
		MasterDetailActivity<CloudApplication, ApplicationsListFragment, ApplicationDetailPager> {
	@Override
	public void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		getSupportActionBar().setDisplayShowTitleEnabled(false);
	}
	
	@Override
	protected ApplicationsListFragment makeLeftFragment() {
		return new ApplicationsListFragment();
	}

	@Override
	protected ApplicationDetailPager makeRightFragment() {
		return new ApplicationDetailPager();
	}   

	
}
