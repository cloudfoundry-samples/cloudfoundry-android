package org.cloudfoundry.android.cfdroid.cloud;

import org.cloudfoundry.android.cfdroid.R;
import org.cloudfoundry.android.cfdroid.support.BaseViewHolder;
import org.cloudfoundry.client.lib.CloudInfo.Runtime;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class RuntimeView extends BaseViewHolder<Runtime> {
	
	private TextView name;
	
	private ImageView logo;
	
	private TextView version;

	public RuntimeView(View view) {
		name = (TextView) view.findViewById(R.id.name);
		logo = (ImageView) view.findViewById(R.id.logo);
		version = (TextView) view.findViewById(R.id.version);
	}

	@Override
	public void bind(Runtime runtime) {
		String runtimeName = runtime.getName();
		RuntimeLogos bestMatch = RuntimeLogos.bestMatch(runtimeName);
		logo.setImageLevel(bestMatch.level);
		version.setText(runtime.getVersion());
		name.setText(runtime.getDescription());
	}

}
