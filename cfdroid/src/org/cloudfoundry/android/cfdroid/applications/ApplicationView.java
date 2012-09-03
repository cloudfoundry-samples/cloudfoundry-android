package org.cloudfoundry.android.cfdroid.applications;

import java.util.EnumMap;

import org.cloudfoundry.android.cfdroid.R;
import org.cloudfoundry.android.cfdroid.support.BaseViewHolder;
import org.cloudfoundry.client.lib.CloudApplication;
import org.cloudfoundry.client.lib.CloudApplication.AppState;

import android.view.View;
import android.widget.TextView;

public class ApplicationView extends BaseViewHolder<CloudApplication> {

	private TextView name;
	private TextView status;

	/*default*/ final static EnumMap<AppState, Integer> COLORS = new EnumMap<CloudApplication.AppState, Integer>(
			AppState.class);
	static {
		COLORS.put(AppState.STARTED, R.color.status_started);
		COLORS.put(AppState.STOPPED, R.color.status_stopped);
		COLORS.put(AppState.UPDATING, R.color.status_updating);
	}

	public ApplicationView(View container) {
		name = (TextView) container.findViewById(R.id.name);
		status = (TextView) container.findViewById(R.id.status);
	}

	@Override
	public void bind(CloudApplication application) {
		name.setText(application.getName());
		AppState state = application.getState();
		status.setText("" + state);
		status.setTextColor(status.getResources().getColor(COLORS.get(state)));
	}
}
