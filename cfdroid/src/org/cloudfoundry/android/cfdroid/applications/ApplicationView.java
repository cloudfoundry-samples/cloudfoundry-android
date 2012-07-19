package org.cloudfoundry.android.cfdroid.applications;

import org.cloudfoundry.android.cfdroid.R;

import android.view.View;
import android.widget.TextView;

public class ApplicationView {

	TextView name;
	
	public ApplicationView(View container) {
		name = (TextView) container.findViewById(R.id.name);
	}

}
