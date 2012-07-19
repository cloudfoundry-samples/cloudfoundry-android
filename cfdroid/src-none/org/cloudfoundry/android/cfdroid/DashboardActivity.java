package org.cloudfoundry.android.cfdroid;

import org.cloudfoundry.android.cfdroid.AppListActivity_;
import org.cloudfoundry.android.cfdroid.ServiceListActivity_;
import org.cloudfoundry.android.cfdroid.menu.AbstractLoggedInActivity_;
import org.cloudfoundry.android.cfdroid.R;

import org.cloudfoundry.android.cfdroid.CloudInfoActivity_;
import org.cloudfoundry.android.cfdroid.LoginActivity_;
import com.googlecode.androidannotations.annotations.AfterInject;
import com.googlecode.androidannotations.annotations.Bean;
import com.googlecode.androidannotations.annotations.Click;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.Extra;
import com.googlecode.androidannotations.annotations.OptionsMenu;

@EActivity(R.layout.dashboard)
public class DashboardActivity extends AbstractLoggedInActivity_ {
	
	@Bean
	Clients clients;
	
	@Extra
	boolean doLogout;

	@Override
	protected void onResume() {
		super.onResume();
		if(doLogout) {
			clients.logout();
		}
		
		if (!clients.isLoggedIn()) {
			LoginActivity_.intent(this).start();
		}
	}
	
	@Click(R.id.db_apps_btn)
	void launchApps() {
		AppListActivity_.intent(this).start();
	}
	
	@Click(R.id.db_services_btn)
	void launchServices() {
		ServiceListActivity_.intent(this).start();
	}
	
	@Click(R.id.db_info_btn)
	void launchInfo() {
		CloudInfoActivity_.intent(this).start();
	}
	
}
