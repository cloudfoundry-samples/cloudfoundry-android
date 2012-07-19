package org.cloudfoundry.android.cfdroid.applications;

import org.cloudfoundry.android.cfdroid.R;
import org.cloudfoundry.android.cfdroid.support.BaseViewHolder;
import org.cloudfoundry.client.lib.CloudApplication;

import android.view.View;
import android.widget.TextView;

public class ApplicationView extends BaseViewHolder<CloudApplication>{

	TextView name;
	
	public ApplicationView(View container) {
		name = (TextView) container.findViewById(R.id.name);
	}
	
	@Override
	public void bind(CloudApplication application) {
		name.setText(application.getName());
	}

}
