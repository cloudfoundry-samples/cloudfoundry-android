package org.cloudfoundry.android.cfdroid.applications;

import org.cloudfoundry.android.cfdroid.R;
import org.cloudfoundry.android.cfdroid.support.BaseViewHolder;
import org.cloudfoundry.client.lib.CloudApplication;

import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class ApplicationView extends BaseViewHolder<CloudApplication>{

	private static final String TAG = "ApplicationView";
	private TextView name;
	private TextView instances;
	private TextView status;
	
	public ApplicationView(View container) {
		name = (TextView) container.findViewById(R.id.name);
		instances = (TextView) container.findViewById(R.id.instances);
		status = (TextView) container.findViewById(R.id.status);
	}
	
	@Override
	public void bind(CloudApplication application) {
		name.setText(application.getName());
		
		int inst = 0;
		try {
		    inst = application.getInstances();
		}catch(Exception e){
			Log.e(TAG,e.toString(),e);
		}
		String state = application.getState().toString();
		instances.setText(inst + "");
		status.setText(state);
		if(state.equals("STARTED")){
			status.setTextColor(Color.rgb(0, 100, 0));
		}else {
			status.setTextColor(Color.rgb(178, 34, 34));
		}
	}

}
