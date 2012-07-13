package org.cloudfoundry.android.cfdroid;

import java.util.List;

import org.cloudfoundry.android.cfdroid.menu.AbstractNotRootActivity_;
import org.cloudfoundry.android.cfdroid.R;
import org.cloudfoundry.client.lib.CloudApplication;

import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.Window;
import org.cloudfoundry.android.cfdroid.ApplicationActivity_;
import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.Bean;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.ItemClick;
import com.googlecode.androidannotations.annotations.NonConfigurationInstance;
import com.googlecode.androidannotations.annotations.OptionsItem;
import com.googlecode.androidannotations.annotations.OptionsMenu;
import com.googlecode.androidannotations.annotations.UiThread;
import com.googlecode.androidannotations.annotations.ViewById;

@EActivity(android.R.layout.list_content)
public class AppListActivity extends AbstractNotRootActivity_ {

	@NonConfigurationInstance
	List<CloudApplication> applications;

	private final class AppListAdapter extends BaseAdapter {

		@Override
		public View getView(int position, View convertView, ViewGroup root) {

			TextView nameView;
			TextView instancesView;
			if (convertView == null) {
				convertView = View.inflate(AppListActivity.this,
						R.layout.application_list_item, null);
				nameView = (TextView) convertView.findViewById(R.id.name);
				instancesView = (TextView) convertView
						.findViewById(R.id.instances);
				convertView.setTag(R.id.name, nameView);
				convertView.setTag(R.id.instances, instancesView);
			} else {
				nameView = (TextView) convertView.getTag(R.id.name);
				instancesView = (TextView) convertView.getTag(R.id.instances);
			}

			nameView.setText(getItem(position).getName());
			instancesView.setText(getItem(position).getRunningInstances() + "/"
					+ getItem(position).getInstances());
			return convertView;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public CloudApplication getItem(int position) {
			return applications == null ? null : applications.get(position);
		}

		@Override
		public int getCount() {
			return applications == null ? 0 : applications.size();
		}
	}

	@Bean
	@NonConfigurationInstance
	ApplicationsDLTask task;

	@ViewById(android.R.id.list)
	ListView appList;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		showDLStatus();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		// Hide the refresh button if DL is in progress
		// This will give the impression that the progress
		// indicator replaces it.
		menu.findItem(R.id.refresh).setVisible(!task.isDlInProgress());
		return true;
	}

	@AfterViews
	void startDLOrShow() {
		appList.setAdapter(new AppListAdapter());

		if (applications == null) {
			task.dl();
		}

	}

	@UiThread
	void updateData(List<CloudApplication> applications) {
		this.applications = applications;
		((BaseAdapter) appList.getAdapter()).notifyDataSetChanged();
	}

	@ItemClick(android.R.id.list)
	void appListItemClicked(CloudApplication application) {
		String html = ("<font color='red'>" + application.getEnvAsMap())
				.replaceAll(",", "<br>") + "</font>";
		Toast.makeText(this, Html.fromHtml(html), Toast.LENGTH_LONG).show();
		
		ApplicationActivity_.intent(this).appName(application.getName()).start();
	}

	@OptionsItem(R.id.refresh)
	void refresh() {
		task.dl();
	}

	@UiThread
	void showDLStatus() {
		setSupportProgressBarIndeterminateVisibility(task.isDlInProgress());
		invalidateOptionsMenu();
	}
}
