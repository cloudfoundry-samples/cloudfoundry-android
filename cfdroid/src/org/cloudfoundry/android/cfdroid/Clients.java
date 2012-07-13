package org.cloudfoundry.android.cfdroid;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.cloudfoundry.client.lib.CloudApplication;
import org.cloudfoundry.client.lib.CloudFoundryClient;
import org.cloudfoundry.client.lib.CloudInfo;
import org.cloudfoundry.client.lib.CloudService;

import com.googlecode.androidannotations.annotations.EBean;
import com.googlecode.androidannotations.api.Scope;

@EBean(scope = Scope.Singleton)
public class Clients {

	private CloudFoundryClient client;

	private Map<String, CloudApplication> applications = new HashMap<String, CloudApplication>();
	
	private Map<String, CloudService> services = new HashMap<String, CloudService>();

	public void store(String login, String target, CloudFoundryClient client) {
		this.client = client;
	}

	public boolean isLoggedIn() {
		return this.client != null;
	}
	
	public List<CloudService> getServices(){
		List<CloudService> ss = client.getServices();
		services.clear();
		for (CloudService service : ss) {
			services.put(service.getName(), service);
		}
		return ss;
	}

	public List<CloudApplication> getApplications() {
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
