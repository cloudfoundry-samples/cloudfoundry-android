package org.cloudfoundry.android.cfdroid.applications;

import org.cloudfoundry.android.cfdroid.support.TabHostFragment;
import org.cloudfoundry.android.cfdroid.support.masterdetail.DataHolder;
import org.cloudfoundry.android.cfdroid.support.masterdetail.DetailPaneEventsCallback;
import org.cloudfoundry.client.lib.CloudApplication;

import android.os.Bundle;
import android.view.View;

public class ApplicationDetailTabs extends TabHostFragment implements
		DetailPaneEventsCallback {

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		
		if (((DataHolder<CloudApplication>) getActivity()).getSelectedItem() != null) {
			addTab("one", ApplicationControlFragment.class);
			addTab("two", ApplicationServicesFragment.class);
			addTab("three", ApplicationControlFragment.class);
			addTab("four", ApplicationServicesFragment.class);
		}
		
	}
	
	@Override
	public void selectionChanged() {

	}

}
