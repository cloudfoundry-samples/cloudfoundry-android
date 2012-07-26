package org.cloudfoundry.android.cfdroid;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Singleton;

import org.cloudfoundry.android.cfdroid.account.Accounts;
import org.cloudfoundry.client.lib.CloudApplication;
import org.cloudfoundry.client.lib.CloudFoundryClient;
import org.cloudfoundry.client.lib.CloudInfo;
import org.cloudfoundry.client.lib.CloudService;

import roboguice.event.ObservesTypeListener.ContextObserverMethodInjector;
import roboguice.util.Ln;

import android.accounts.AccountManager;
import android.accounts.AccountManagerFuture;
import android.app.Activity;
import android.database.ContentObservable;
import android.database.ContentObserver;
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

		private Map<String, CloudApplication> applications = new LinkedHashMap<String, CloudApplication>();

		private Map<String, CloudService> services = new LinkedHashMap<String, CloudService>();

		private ContentObservable applicationsObservable = new ContentObservable();

		private ContentObservable servicesObservavle = new ContentObservable();

		private void updateApp(CloudApplication app) {
			applications.put(app.getName(), app);
			applicationsObservable.notifyChange(false);
		}

		private void fetchApps() {
			applications.clear();
			for (CloudApplication app : client.getApplications()) {
				applications.put(app.getName(), app);
			}
			applicationsObservable.notifyChange(false);
		}

		private void fetchServices() {
			services.clear();
			for (CloudService service : client.getServices()) {
				services.put(service.getName(), service);
			}
			servicesObservavle.notifyChange(false);
		}

	}

	@Inject
	private CloudFoundry(Activity activity, AccountManager accountManager,
			Cache cache) {
		this.activity = activity;
		this.cache = cache;
		this.accountManager = accountManager;
	}

	public List<CloudApplication> getApplications(boolean force) {
		ensureClient();
		if (force) {
			cache.fetchApps();
		}
		return new ArrayList<CloudApplication>(cache.applications.values());
	}

	public List<CloudService> getServices(boolean force) {
		ensureClient();
		if (force) {
			cache.fetchServices();
		}
		return new ArrayList<CloudService>(cache.services.values());
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

	public void updateApplicationInstances(String appName,
			int instances) {
		cache.client.updateApplicationInstances(appName, instances);
		cache.updateApp(cache.client.getApplication(appName));
	}

	private void ensureClient() {
		if (cache.client != null) {
			return;
		}
		try {
			AccountManagerFuture<Bundle> future = accountManager
					.getAuthTokenByFeatures(Accounts.ACCOUNT_TYPE,
							Accounts.ACCOUNT_TYPE, new String[0], activity,
							null, null, null, null);
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

	public void updateApplicationMemory(String name, int memory) {
		cache.client.updateApplicationMemory(name, memory);
		cache.updateApp(cache.client.getApplication(name));
	}

	public void startApplication(String name) {
		cache.client.startApplication(name);
		cache.updateApp(cache.client.getApplication(name));
	}

	public void stopApplication(String name) {
		cache.client.stopApplication(name);
		cache.updateApp(cache.client.getApplication(name));
	}

	public int[] getApplicationMemoryChoices() {
		// Clear cloudInfo after, as it is populated by side effect
		int[] result = cache.client.getApplicationMemoryChoices();
		clearCloudInfoField();
		return result;
	}

	public void listenForApplicationsUpdates(ContentObserver observer) {
		cache.applicationsObservable.registerObserver(observer);
	}

	public void stopListeningForApplicationUpdates(ContentObserver observer) {
		cache.applicationsObservable.unregisterObserver(observer);
	}

	public void listenForServicesUpdates(ContentObserver observer) {
		cache.servicesObservavle.registerObserver(observer);
	}

	public void stopListeningForServicesUpdates(ContentObserver observer) {
		cache.servicesObservavle.unregisterObserver(observer);
	}

}
