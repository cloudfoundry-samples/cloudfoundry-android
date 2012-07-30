package org.cloudfoundry.android.cfdroid.applications;

import org.cloudfoundry.android.cfdroid.R;
import org.cloudfoundry.android.cfdroid.support.TabHostFragment;
import org.cloudfoundry.android.cfdroid.support.masterdetail.DataHolder;
import org.cloudfoundry.android.cfdroid.support.masterdetail.DetailPaneEventsCallback;
import org.cloudfoundry.client.lib.CloudApplication;

import roboguice.inject.InjectResource;
import roboguice.util.Ln;

public class ApplicationDetailTabs extends TabHostFragment implements
		DetailPaneEventsCallback {

	@InjectResource(R.string.control)
	private String controlLabel;

	@InjectResource(R.string.services)
	private String servicesLabel;

	@InjectResource(R.string.instances)
	private String instancesLabel;

	@InjectResource(R.string.application_info)
	private String infoLabel;
	
	@Override
	protected void setupTabs() {
		if (((DataHolder<CloudApplication>) getActivity()).getSelectedItem() != null) {
			addTab("control", controlLabel, ApplicationControlFragment.class);
			addTab("services", servicesLabel, ApplicationServicesFragment.class);
			addTab("stats", instancesLabel, ApplicationInstanceStatsFragment.class);
//			addTab("info", infoLabel, ApplicationInfoFragment.class);
		}
	}

	@Override
	public void selectionChanged() {
		clearAll();
		setupTabs();
	}

}
