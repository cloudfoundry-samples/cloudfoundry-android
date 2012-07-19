package org.cloudfoundry.android.cfdroid;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.cloudfoundry.android.cfdroid.account.CloudFoundryAccountConstants;
import org.cloudfoundry.client.lib.CloudApplication;
import org.cloudfoundry.client.lib.CloudFoundryClient;
import org.cloudfoundry.client.lib.CloudInfo;
import org.cloudfoundry.client.lib.CloudService;

import roboguice.util.Ln;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.os.Bundle;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class Clients {

	@Inject
	private AccountManager accountManager;

	@Inject
	private Activity currentActivity;

	private CloudFoundryClient client;

	private Map<String, CloudApplication> applications = new HashMap<String, CloudApplication>();

	private Map<String, CloudService> services = new HashMap<String, CloudService>();

	public void store(String login, String target, CloudFoundryClient client) {
		this.client = client;
	}

	public boolean isLoggedIn() {
		// return this.client != null;
		Account[] accounts = accountManager
				.getAccountsByType(CloudFoundryAccountConstants.ACCOUNT_TYPE);
		Account account = accounts.length >= 1 ? accounts[0] : null;

		if (account == null) {
			return false;
		}
		try {

			String token = accountManager.blockingGetAuthToken(account,
					CloudFoundryAccountConstants.ACCOUNT_TYPE, true);
			Ln.d(token);

		} catch (Exception e) {
			Ln.e(e);
		}

		return false;

	}

	private void ensureClient() {
		if (client != null) {
			return;
		}
		try {
			Bundle bundle = accountManager.getAuthTokenByFeatures(
					CloudFoundryAccountConstants.ACCOUNT_TYPE,
					CloudFoundryAccountConstants.ACCOUNT_TYPE, new String[0],
					currentActivity, null, null, null, null).getResult();

			String[] parts = bundle.getString(AccountManager.KEY_ACCOUNT_NAME)
					.split("\\|");
			String token = bundle.getString(AccountManager.KEY_AUTHTOKEN);

			client = new CloudFoundryClient(token, parts[1]);
		} catch (Exception e) {
			Ln.e(e);
		}
	}

	public List<CloudService> getServices() {
		List<CloudService> ss = client.getServices();
		services.clear();
		for (CloudService service : ss) {
			services.put(service.getName(), service);
		}
		return ss;
	}

	public List<CloudApplication> getApplications() {
		ensureClient();

		List<CloudApplication> apps = client.getApplications();
		applications.clear();
		for (CloudApplication app : apps) {
			applications.put(app.getName(), app);
		}
		return apps;
	}

	public CloudApplication getApplication(String name) {
		return applications.get(name);
	}

	public CloudService getService(String name) {
		return services.get(name);
	}

	public CloudInfo getCloudInfo() {
		return client.getCloudInfo();
	}

	public void logout() {
		this.client = null;
		applications.clear();
		services.clear();
	}

}
