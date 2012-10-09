/*
 * Copyright 2012 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.cloudfoundry.android.cfdroid;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import javax.inject.Singleton;

import org.cloudfoundry.android.cfdroid.account.Accounts;
import org.cloudfoundry.client.lib.CloudApplication;
import org.cloudfoundry.client.lib.CloudFoundryClient;
import org.cloudfoundry.client.lib.CloudFoundryException;
import org.cloudfoundry.client.lib.CloudInfo;
import org.cloudfoundry.client.lib.CloudService;
import org.cloudfoundry.client.lib.InstanceStats;
import org.cloudfoundry.client.lib.ServiceConfiguration;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.RestClientException;

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

	@Singleton
	private static class Cache {
		private Map<String, CloudApplication> applications = new LinkedHashMap<String, CloudApplication>();

		private ContentObservable applicationsObservable = new ContentObservable();

		private CloudFoundryClient client;
		
		/**
		 * Last known token (may be stale).
		 */
		private String token;

		private Map<String, CloudService> services = new LinkedHashMap<String, CloudService>();

		private ContentObservable servicesObservavle = new ContentObservable();

		private List<ServiceConfiguration> serviceConfigurations = null;

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

		private void updateApp(CloudApplication app) {
			applications.put(app.getName(), app);
			applicationsObservable.notifyChange(false);
		}

	}

	private final AccountManager accountManager;

	private final Activity activity;

	private int attempt;

	private final Cache cache;

	@Inject
	private CloudFoundry(Activity activity, AccountManager accountManager,
			Cache cache) {
		this.activity = activity;
		this.cache = cache;
		this.accountManager = accountManager;
	}

	public CloudApplication bindService(final String application,
			final String service) {
		return doWithClient(new Callable<CloudApplication>() {
			@Override
			public CloudApplication call() throws Exception {
				cache.client.bindService(application, service);
				// Force a reload of that CloudApplication object
				// to reflect the change.
				CloudApplication app = cache.client.getApplication(application);
				cache.updateApp(app);
				return app;
			}
		});
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

	/**
	 * Ensures that a proper {@link CloudFoundryClient} exists and handles
	 * connection errors.
	 */

	private <R> R doWithClient(Callable<R> work) {
		ensureClient();
		try {
			return work.call();
		} catch (CloudFoundryException e) {
			// Landing here surely means that
			// our client object holds a stale token.
			// Throw it away and retry.
			if (attempt == 0 && e.getStatusCode() == HttpStatus.FORBIDDEN) {
				Ln.w(e,
						"Caught exception for the first time. Assuming stale token, will retry.");
				attempt++;
				cache.client = null;
				accountManager.invalidateAuthToken(Accounts.ACCOUNT_TYPE, cache.token);
				cache.token = null;
				R result = doWithClient(work);
				attempt--;
				return result;
			} else {
				Ln.w(e, "Caught exception for the second time. Rethrowing");
				throw e;
			}
		} catch (RuntimeException e) {
			throw e;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Makes sure that we have a reference to a non-null {@link CloudFoundryClient}
	 * object.
	 * 
	 */
	private void ensureClient() {
		if (cache.client != null) {
			return ;
		}
		try {
			AccountManagerFuture<Bundle> future = accountManager
					.getAuthTokenByFeatures(Accounts.ACCOUNT_TYPE,
							Accounts.ACCOUNT_TYPE, new String[0], activity,
							null, null, null, null);
			Bundle bundle = future.getResult();
			String targetURL = Accounts.extractTarget(bundle
					.getString(AccountManager.KEY_ACCOUNT_NAME));
			cache.token = bundle.getString(AccountManager.KEY_AUTHTOKEN);

			cache.client = new CloudFoundryClient(cache.token, targetURL);
		} catch (Exception e) {
			Ln.e(e, "Logged from here");
		}
	}

	public CloudApplication getApplication(String name) {
		return cache.applications.get(name);
	}

	public int[] getApplicationMemoryChoices() {
		return doWithClient(new Callable<int[]>() {
			@Override
			public int[] call() throws Exception {
				// Clear cloudInfo after, as it is populated by side effect
				int[] result = cache.client.getApplicationMemoryChoices();
				clearCloudInfoField();
				return result;
			}
		});
	}

	public List<CloudApplication> getApplications(final boolean force) {
		return doWithClient(new Callable<List<CloudApplication>>() {
			@Override
			public List<CloudApplication> call() throws Exception {
				if (force) {
					cache.fetchApps();
				}
				return new ArrayList<CloudApplication>(
						cache.applications.values());
			}
		});

	}

	public List<InstanceStats> getApplicationStats(final String appName) {
		return doWithClient(new Callable<List<InstanceStats>>() {
			@Override
			public List<InstanceStats> call() throws Exception {
				return cache.client.getApplicationStats(appName).getRecords();
			}
		});
	}

	public CloudInfo getCloudInfo() {
		return doWithClient(new Callable<CloudInfo>() {
			@Override
			public CloudInfo call() throws Exception {
				CloudInfo cloudInfo = cache.client.getCloudInfo();
				clearCloudInfoField();
				return cloudInfo;
			}
		});
	}

	public List<CloudService> getServices(final boolean force) {
		return doWithClient(new Callable<List<CloudService>>() {
			@Override
			public List<CloudService> call() throws Exception {
				if (force) {
					cache.fetchServices();
				}
				return new ArrayList<CloudService>(cache.services.values());
			}
		});
	}

	public List<ServiceConfiguration> getServiceConfigurations() {
		if (cache.serviceConfigurations == null) {
			return doWithClient(new Callable<List<ServiceConfiguration>>() {
				@Override
				public List<ServiceConfiguration> call() throws Exception {
					cache.serviceConfigurations = cache.client
							.getServiceConfigurations();
					return cache.serviceConfigurations;
				}

			});
		} else {
			return cache.serviceConfigurations;
		}
	}

	public void listenForApplicationsUpdates(ContentObserver observer) {
		cache.applicationsObservable.registerObserver(observer);
	}

	public void listenForServicesUpdates(ContentObserver observer) {
		cache.servicesObservavle.registerObserver(observer);
	}

	public void startApplication(final String name) {
		doWithClient(new Callable<Void>() {
			@Override
			public Void call() throws Exception {
				cache.client.startApplication(name);
				cache.updateApp(cache.client.getApplication(name));
				return null;
			}
		});
	}

	public void stopApplication(final String name) {
		doWithClient(new Callable<Void>() {
			@Override
			public Void call() throws Exception {
				cache.client.stopApplication(name);
				cache.updateApp(cache.client.getApplication(name));
				return null;
			}
		});
	}

	public void stopListeningForApplicationUpdates(ContentObserver observer) {
		cache.applicationsObservable.unregisterObserver(observer);
	}

	public void stopListeningForServicesUpdates(ContentObserver observer) {
		cache.servicesObservavle.unregisterObserver(observer);
	}

	public CloudApplication unbindService(final String application,
			final String service) {
		return doWithClient(new Callable<CloudApplication>() {
			@Override
			public CloudApplication call() throws Exception {
				cache.client.unbindService(application, service);
				// Force a reload of that CloudApplication object
				// to reflect the change.
				CloudApplication app = cache.client.getApplication(application);
				cache.updateApp(app);
				return app;
			}
		});
	}

	public void updateApplicationInstances(final String appName,
			final int instances) {
		doWithClient(new Callable<Void>() {
			@Override
			public Void call() throws Exception {
				cache.client.updateApplicationInstances(appName, instances);
				cache.updateApp(cache.client.getApplication(appName));
				return null;
			}
		});

	}

	public void updateApplicationMemory(final String name, final int memory) {
		doWithClient(new Callable<Void>() {
			@Override
			public Void call() throws Exception {
				cache.client.updateApplicationMemory(name, memory);
				cache.updateApp(cache.client.getApplication(name));
				return null;
			}
		});
	}

	public void createService(final String name, final ServiceConfiguration sc) {
		// Be safe for now
		if (sc.getTiers().size() != 1) {
			throw new IllegalStateException();
		}
		doWithClient(new Callable<Void>() {
			@Override
			public Void call() throws Exception {
				CloudService s = new CloudService();
				s.setName(name);
				s.setVendor(sc.getVendor());
				s.setVersion(sc.getVersion());
				s.setType(sc.getType());
				s.setTier(sc.getTiers().get(0).getType());
				cache.client.createService(s);

				cache.fetchServices();
				return null;
			}
		});
	}

}
