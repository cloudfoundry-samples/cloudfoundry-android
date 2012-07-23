package org.cloudfoundry.android.cfdroid.services;

import org.cloudfoundry.android.cfdroid.R;
import org.cloudfoundry.android.cfdroid.support.BaseViewHolder;
import org.cloudfoundry.client.lib.CloudService;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class ServiceView extends BaseViewHolder<CloudService> {
	
	protected TextView name;
	
	protected ImageView logo;
	
	public ServiceView(View container) {
		super();
		name = (TextView) container.findViewById(R.id.name);
		logo = (ImageView) container.findViewById(R.id.logo);
	}

	@Override
	public void bind(CloudService service) {
		name.setText(service.getName());
		ServiceLogos logoEnum;
		try {
			 logoEnum = ServiceLogos.valueOf(service.getVendor());
		} catch (IllegalArgumentException notFound) {
			logoEnum = ServiceLogos.unknown;
		}
		logo.setImageLevel(logoEnum.level);
	}

}
