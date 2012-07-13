package org.cloudfoundry.android.cfdroid;

import java.util.List;

import org.cloudfoundry.android.cfdroid.menu.AbstractNotRootActivity_;
import org.cloudfoundry.android.cfdroid.R;
import org.cloudfoundry.client.lib.CloudService;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.Window;
import org.cloudfoundry.android.cfdroid.ServiceActivity_;
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
public class ServiceListActivity extends AbstractNotRootActivity_ {

	@NonConfigurationInstance
	List<CloudService> services;

	private final class ServiceListAdapter extends BaseAdapter {

		@Override
		public View getView(int position, View convertView, ViewGroup root) {

			TextView nameView;
			if (convertView == null) {
				convertView = View.inflate(ServiceListActivity.this,
						R.layout.service_list_item, null);
				nameView = (TextView) convertView.findViewById(R.id.name);
				convertView.setTag(R.id.name, nameView);
			} else {
				nameView = (TextView) convertView.getTag(R.id.name);
			}

			nameView.setText(getItem(position).getName());
			return convertView;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public CloudService getItem(int position) {
			return services == null ? null : services.get(position);
		}

		@Override
		public int getCount() {
			return services == null ? 0 : services.size();
		}
	}

	@Bean
	@NonConfigurationInstance
	ServicesDLTask task;

	@ViewById(android.R.id.list)
	ListView serviceList;

	@Bean
	Clients clients;

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
		serviceList.setAdapter(new ServiceListAdapter());

		if (services == null) {
			task.dl();
		}

	}

	@UiThread
	void updateData(List<CloudService> services) {
		this.services = services;
		((BaseAdapter) serviceList.getAdapter()).notifyDataSetChanged();
	}

	@ItemClick(android.R.id.list)
	void appListItemClicked(CloudService service) {
		ServiceActivity_.intent(this).serviceName(service.getName()).start();
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
