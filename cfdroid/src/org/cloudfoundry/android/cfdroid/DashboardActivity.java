package org.cloudfoundry.android.cfdroid;

import org.cloudfoundry.android.cfdroid.applications.ApplicationsActivity;
import org.cloudfoundry.android.cfdroid.cloud.CloudInfoActivity;

import roboguice.inject.ContentView;
import roboguice.inject.InjectView;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.github.rtyley.android.sherlock.roboguice.activity.RoboSherlockActivity;

@ContentView(R.layout.dashboard)
public class DashboardActivity extends RoboSherlockActivity {
	
	@InjectView(R.id.db_apps_btn)
	private View apps;
	
	@InjectView(R.id.db_services_btn)
	private View services;
	
	@InjectView(R.id.db_info_btn)
	private View cloud;
	
	@InjectView(R.id.db_info_btn)
	private View about;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		apps.setOnClickListener(launch(ApplicationsActivity.class));
		cloud.setOnClickListener(launch(CloudInfoActivity.class));
	}
	
	
	private OnClickListener launch(final Class<? extends Activity> klass) {
		return new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(DashboardActivity.this, klass);
				startActivity(intent);
				
			}
		};
	}

}
