package org.cloudfoundry.android.cfdroid;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Singleton;

import org.cloudfoundry.android.cfdroid.account.Accounts;
import org.cloudfoundry.client.lib.CloudApplication;
import org.cloudfoundry.client.lib.CloudFoundryClient;
import org.cloudfoundry.client.lib.CloudInfo;
import org.cloudfoundry.client.lib.CloudService;

import roboguice.util.Ln;

import android.accounts.AccountManager;
import android.accounts.AccountManagerFuture;
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
		CloudInfo cloudInfo = cache.client.getCloudInfo();
		clearCloudInfoField();
		return cloudInfo;
	}

	private void clearCloudInfoField() {
		// CFC.getCloudInfo() currently caches its result, while
		// some parts of it could actually change (mem usage).
		// We forcibly clear it here... dirty
		Field f;
		try {
			f = CloudFoundryClient.class.getDeclaredField("info");
			f.setAccessible(true);
			f.set(cache.client, null);
		} catch (NoSuchFieldException e) {
			throw new RuntimeException(e);
		} catch (IllegalArgumentException e) {
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
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
			AccountManagerFuture<Bundle> future = accountManager.getAuthTokenByFeatures(
					Accounts.ACCOUNT_TYPE,
					Accounts.ACCOUNT_TYPE, new String[0],
					activity, null, null, null, null);
			Bundle bundle = future.getResult();
			Ln.i("Do I ever get there? %s", bundle);

			String targetURL = Accounts.extractTarget(bundle
					.getString(AccountManager.KEY_ACCOUNT_NAME));
			String token = bundle.getString(AccountManager.KEY_AUTHTOKEN);

			cache.client = new CloudFoundryClient(token, targetURL);
		} catch (Exception e) {
			Ln.e(e, "Logged from here");
		}
	}

	public CloudApplication updateApplicationMemory(String name, int memory) {
		cache.client.updateApplicationMemory(name, memory);
		return cache.client.getApplication(name);
	}
	
	public CloudApplication startApplication(String name) {
		cache.client.startApplication(name);
		return cache.client.getApplication(name);
	}

	public CloudApplication stopApplication(String name) {
		cache.client.stopApplication(name);
		return cache.client.getApplication(name);
	}

	public int[] getApplicationMemoryChoices() {
		// Clear cloudInfo after, as it is populated by side effect
		int[] result = cache.client.getApplicationMemoryChoices();
		clearCloudInfoField();
		return result;
	}

}
