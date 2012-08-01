package org.cloudfoundry.android.cfdroid.cloud;

import org.cloudfoundry.android.cfdroid.R;
import org.cloudfoundry.android.cfdroid.support.BaseViewHolder;
import org.cloudfoundry.client.lib.CloudInfo.Framework;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class FrameworkView extends BaseViewHolder<Framework> {
	
	private ImageView logo;
	
	private TextView name;

	public FrameworkView(View container) {
		logo = (ImageView) container.findViewById(R.id.logo);
		name = (TextView) container.findViewById(R.id.name);
	}

	@Override
	public void bind(Framework item) {
		FrameworkLogos fl = FrameworkLogos.unknown;
		try {
			 fl = FrameworkLogos.valueOf(item.getName());
		} catch (IllegalArgumentException ignore) {
		}
		logo.setImageLevel(fl.level);
		name.setText(item.getName());
	}

}
