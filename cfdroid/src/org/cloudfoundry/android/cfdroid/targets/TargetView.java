package org.cloudfoundry.android.cfdroid.targets;

import org.cloudfoundry.android.cfdroid.R;

import android.view.View;
import android.widget.TextView;

public class TargetView {

	protected TextView label;
	protected TextView url;
	
	public TargetView(View container) {
		label = (TextView)container.findViewById(R.id.label);
		url = (TextView)container.findViewById(R.id.url);
	}
	
	public void bind(CloudTarget target) {
		label.setText(target.getLabel());
		url.setText(target.getURL());
	}

}
