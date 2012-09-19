package org.cloudfoundry.android.cfdroid.applications;

import java.util.List;

import org.cloudfoundry.android.cfdroid.R;
import org.cloudfoundry.android.cfdroid.support.masterdetail.MasterDetailActivity;
import org.cloudfoundry.client.lib.CloudApplication;

import roboguice.inject.ContentView;
import android.os.Bundle;

/**
 * Master/Detail activity for applications. Shows a list of applications and
 * details about one on the next/right screen.
 * 
 * @author Eric Bottard
 * 
 */
@ContentView(R.layout.applications)
public class ApplicationsActivity
		extends
		MasterDetailActivity<ApplicationDetailPager> {
	@Override
	public void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		getSupportActionBar().setDisplayShowTitleEnabled(false);
	}

	@Override
	protected int rightPaneLayout() {
		return R.layout.right_pane_applications;
	}

}
