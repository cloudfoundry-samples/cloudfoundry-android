package org.cloudfoundry.android.cfdroid;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Singleton;

import org.cloudfoundry.android.cfdroid.account.Accounts;
import org.cloudfoundry.android.cfdroid.account.CloudFoundryAccountConstants;
import org.cloudfoundry.client.lib.CloudApplication;
import org.cloudfoundry.client.lib.CloudFoundryClient;
import org.cloudfoundry.client.lib.CloudInfo;
import org.cloudfoundry.client.lib.CloudService;

import roboguice.util.Ln;

import android.accounts.AccountManager;
import android.app.Activity;
import android.os.Bundle;

import com.google.inject.Inject;

/**
 * A wrapper around {@link CloudFoundryClient} that allows
 * <ul>
 * <li>prompting for an account if not already set</li>
 * <li>caching state returned by the cloud controller to avoid roundtrips to the
 * server.</li>
 * </ul>
 * 
 * <strong>Note that this class is not Singleton scoped, while Cache
 * is.</strong> This is to avoid leaking activities.
 * 
 * @author Eric Bottard
 */
public class CloudFoundry {

	private final Activity activity;

	private final Cache cache;

	private final AccountManager accountManager;

	@Singleton
	private static class Cache {
		private CloudFoundryClient client;

		private Map<String, CloudApplication> applications = new HashMap<String, CloudApplication>();

		private Map<String, CloudService> services = new HashMap<String, CloudService>();

	}

	@Inject
	private CloudFoundry(Activity activity, AccountManager accountManager,
			Cache cache) {
		this.activity = activity;
		this.cache = cache;
		this.accountManager = accountManager;
	}
	
	public List<CloudApplication> getApplications() {
		ensureClient();
		return cache.client.getApplications();
	}
	
	public List<CloudService> getServices() {
		ensureClient();
		return cache.client.getServices();
	}
	
	public CloudInfo getCloudInfo() {
		// TODO reflectively clear cached data inside cache.client
		return cache.client.getCloudInfo();
	}

	public CloudApplication updateApplicationInstances(String appName, int instances) {
		cache.client.updateApplicationInstances(appName, instances);
		return cache.client.getApplication(appName);
	}
	
	private void ensureClient() {
		if (cache.client != null) {
			return;
		}
		try {
			Bundle bundle = accountManager.getAuthTokenByFeatures(
					CloudFoundryAccountConstants.ACCOUNT_TYPE,
					CloudFoundryAccountConstants.ACCOUNT_TYPE, new String[0],
					activity, null, null, null, null).getResult();

			String targetURL = Accounts.extractTarget(bundle
					.getString(AccountManager.KEY_ACCOUNT_NAME));
			String token = bundle.getString(AccountManager.KEY_AUTHTOKEN);

			cache.client = new CloudFoundryClient(token, targetURL);

			// TODO hack for no network on main for now
			cache.client.getCloudInfo();
		} catch (Exception e) {
			Ln.e(e, "Logged from here");
		}
	}

	public CloudApplication updateApplicationMemory(String name, int memory) {
		cache.client.updateApplicationMemory(name, memory);
		return cache.client.getApplication(name);
	}

	public int[] getApplicationMemoryChoices() {
		// TODO clear cloudInfo after, as it is populated by side effect
		return cache.client.getApplicationMemoryChoices();
	}

}
