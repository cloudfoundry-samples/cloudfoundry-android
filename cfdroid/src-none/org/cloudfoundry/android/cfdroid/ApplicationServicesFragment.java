package org.cloudfoundry.android.cfdroid;

import java.util.List;

import org.cloudfoundry.android.cfdroid.R;
import org.cloudfoundry.client.lib.CloudApplication;

import android.support.v4.app.Fragment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.EFragment;
import com.googlecode.androidannotations.annotations.ViewById;
import com.googlecode.androidannotations.annotations.res.StringRes;

@EFragment(R.layout.application_services_fragment)
public class ApplicationServicesFragment extends Fragment implements HasTitle{
	

	@ViewById(R.id.app_screen_service_list)
	ListView services;
	
	@StringRes(R.string.services)
	String title;
	
	private CloudApplication getCloudApplication() {
		return ((ApplicationActivity) getActivity()).getCloudApplication();
	}
	
	private class ServiceListAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			List<String> ss = getCloudApplication().getServices();
			return ss == null ? 0 : ss.size();
		}

		@Override
		public String getItem(int position) {
			List<String> ss = getCloudApplication().getServices();
			return ss == null ? null : ss.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			TextView nameView;
			if (convertView == null) {
				convertView = View.inflate(getActivity(),
						R.layout.service_list_item, null);
				nameView = (TextView) convertView.findViewById(R.id.name);
				convertView.setTag(R.id.name, nameView);
			} else {
				nameView = (TextView) convertView.getTag(R.id.name);
			}

			nameView.setText(getItem(position));
			return convertView;
		}
		
	}
	
	@AfterViews
	void display() {
		services.setAdapter(new ServiceListAdapter());
	}

	@Override
	public CharSequence getTitle() {
		return title;
	}
}
