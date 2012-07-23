package org.cloudfoundry.android.cfdroid.targets;

import org.cloudfoundry.android.cfdroid.R;
import org.cloudfoundry.android.cfdroid.support.BaseViewHolder;

import android.view.View;
import android.widget.TextView;

public class TargetView extends BaseViewHolder<CloudTarget>{

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
