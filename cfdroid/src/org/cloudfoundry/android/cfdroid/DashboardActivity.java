package org.cloudfoundry.android.cfdroid;

import org.cloudfoundry.android.cfdroid.account.CloudFoundryAccountConstants;
import org.cloudfoundry.android.cfdroid.menu.AbstractLoggedInActivity;

import roboguice.inject.ContentView;
import roboguice.inject.InjectExtra;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Intent;

import com.google.inject.Inject;

@ContentView(R.layout.dashboard)
public class DashboardActivity extends AbstractLoggedInActivity {

	@Inject
	Clients clients;

	@InjectExtra(value = "doLogout", optional = true)
	boolean doLogout;

	@Inject
	private AccountManager accountManager;

	@Override
	protected void onResume() {
		super.onResume();
		if (doLogout) {
			clients.logout();
		}

		if (!clients.isLoggedIn()) {
			startActivity(new Intent(this, LoginActivity.class));
		}
	}

	/*
	 * 
	 * @Click(R.id.db_apps_btn) void launchApps() {
	 * AppListActivity_.intent(this).start(); }
	 * 
	 * @Click(R.id.db_services_btn) void launchServices() {
	 * ServiceListActivity_.intent(this).start(); }
	 * 
	 * @Click(R.id.db_info_btn) void launchInfo() {
	 * CloudInfoActivity_.intent(this).start(); }
	 */
}
