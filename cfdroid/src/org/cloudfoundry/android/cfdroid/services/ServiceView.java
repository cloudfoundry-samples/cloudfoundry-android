package org.cloudfoundry.android.cfdroid.services;

import org.cloudfoundry.android.cfdroid.R;
import org.cloudfoundry.android.cfdroid.support.BaseViewHolder;
import org.cloudfoundry.client.lib.CloudService;

import android.view.View;
import android.widget.TextView;

public class ServiceView extends BaseViewHolder<CloudService> {
	
	protected TextView name;
	
	public ServiceView(View container) {
		super();
		name = (TextView) container.findViewById(R.id.name);
	}

	@Override
	public void bind(CloudService service) {
		name.setText(service.getName());
	}

}
