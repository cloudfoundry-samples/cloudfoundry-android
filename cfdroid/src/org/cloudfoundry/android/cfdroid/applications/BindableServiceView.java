package org.cloudfoundry.android.cfdroid.applications;

import org.cloudfoundry.android.cfdroid.R;
import org.cloudfoundry.android.cfdroid.services.ServiceView;
import org.cloudfoundry.client.lib.CloudApplication;
import org.cloudfoundry.client.lib.CloudService;

import android.view.View;
import android.widget.ToggleButton;

/**
 * A view that shows info about a service as well as toggle button to
 * bind/unbind the service to a given app.
 * 
 * @author ebottard
 * 
 */
public class BindableServiceView extends ServiceView {

	private CloudApplication cloudApplication;

	private ToggleButton bindButton;

	public ToggleButton getBindButton() {
		return bindButton;
	}

	public BindableServiceView(View container, CloudApplication cloudApplication) {
		super(container);
		this.cloudApplication = cloudApplication;
		bindButton = (ToggleButton) container.findViewById(R.id.link_unlink);
	}

	@Override
	public void bind(CloudService service) {
		super.bind(service);

		boolean bound = cloudApplication.getServices().contains(
				service.getName());
		bindButton.setChecked(bound);
	}

}
