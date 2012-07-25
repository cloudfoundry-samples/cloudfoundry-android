package org.cloudfoundry.android.cfdroid.applications;

import org.cloudfoundry.android.cfdroid.R;
import org.cloudfoundry.android.cfdroid.support.TabHostFragment;
import org.cloudfoundry.android.cfdroid.support.masterdetail.DataHolder;
import org.cloudfoundry.android.cfdroid.support.masterdetail.DetailPaneEventsCallback;
import org.cloudfoundry.client.lib.CloudApplication;

import roboguice.inject.InjectResource;

public class ApplicationDetailTabs extends TabHostFragment implements
		DetailPaneEventsCallback {

	@InjectResource(R.string.control)
	private String controlLabel;

	@InjectResource(R.string.services)
	private String servicesLabel;

	@Override
	protected void setupTabs() {
		if (((DataHolder<CloudApplication>) getActivity()).getSelectedItem() != null) {
			addTab("control", controlLabel, ApplicationControlFragment.class);
			addTab("services", servicesLabel, ApplicationServicesFragment.class);
		}
	}

	@Override
	public void selectionChanged() {
		clearAll();
		setupTabs();
	}

}
