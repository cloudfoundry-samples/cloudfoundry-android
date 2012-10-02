package org.cloudfoundry.android.cfdroid.services;

import org.cloudfoundry.android.cfdroid.R;
import org.cloudfoundry.android.cfdroid.support.BaseViewHolder;
import org.cloudfoundry.client.lib.ServiceConfiguration;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * A view dedicated to information about available services.
 * 
 * @author Eric Bottard
 *
 */
public class ServiceConfigurationView extends
		BaseViewHolder<ServiceConfiguration> {

	private ImageView logo;
	private TextView label;

	public ServiceConfigurationView(View container) {
		logo = (ImageView) container.findViewById(R.id.logo);
		label = (TextView) container.findViewById(R.id.name);
	}

	@Override
	public void bind(ServiceConfiguration serviceConfig) {
		label.setText(serviceConfig.getVendor());
		ServiceLogos logoEnum;
		try {
			logoEnum = ServiceLogos.valueOf(serviceConfig.getVendor());
		} catch (IllegalArgumentException notFound) {
			logoEnum = ServiceLogos.unknown;
		}
		logo.setImageLevel(logoEnum.level);
	}

}