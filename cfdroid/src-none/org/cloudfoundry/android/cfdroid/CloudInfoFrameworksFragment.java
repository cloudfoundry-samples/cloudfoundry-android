package org.cloudfoundry.android.cfdroid;

import java.util.ArrayList;
import java.util.List;

import org.cloudfoundry.android.cfdroid.R;
import org.cloudfoundry.client.lib.CloudInfo;

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

@EFragment(android.R.layout.list_content)
public class CloudInfoFrameworksFragment extends Fragment implements HasTitle {

	@ViewById(android.R.id.list)
	ListView listView;
	
	@StringRes(R.string.ci_info_frameworks)
	String title;
	
	@Override
	public CharSequence getTitle() {
		return title;
	}

	@AfterViews
	void afterViews() {
		listView.setAdapter(new FrameworkListAdapter());
	}

	private List<CloudInfo.Framework> frameworks = new ArrayList<CloudInfo.Framework>();

	void updateData(CloudInfo info) {
		frameworks = new ArrayList<CloudInfo.Framework>(info.getFrameworks());
		if (listView != null && listView.getAdapter() != null) {
			((BaseAdapter) listView.getAdapter()).notifyDataSetChanged();
		}
	}

	private final class FrameworkListAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return frameworks.size();
		}

		@Override
		public CloudInfo.Framework getItem(int position) {
			return frameworks.get(position);
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
						android.R.layout.simple_list_item_1, null);
				nameView = (TextView) convertView
						.findViewById(android.R.id.text1);
				convertView.setTag(R.id.name, nameView);
			} else {
				nameView = (TextView) convertView.getTag(R.id.name);
			}

			nameView.setText(getItem(position).getName());
			return convertView;
		}

	}
}
