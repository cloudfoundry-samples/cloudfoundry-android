package org.cloudfoundry.android.cfdroid.applications;

import org.cloudfoundry.android.cfdroid.R;
import org.cloudfoundry.android.cfdroid.support.masterdetail.MasterDetailActivity;
import org.cloudfoundry.client.lib.CloudApplication;

import roboguice.inject.ContentView;

@ContentView(R.layout.applications)
public class ApplicationsActivity
		extends
		MasterDetailActivity<CloudApplication, ApplicationsListFragment, ApplicationDetailTabs> {

	@Override
	protected ApplicationsListFragment makeLeftFragment() {
		return new ApplicationsListFragment();
	}

	@Override
	protected ApplicationDetailTabs makeRightFragment() {
		//return new ApplicationDetailViewPager();
		return new ApplicationDetailTabs();
	}

	
}
