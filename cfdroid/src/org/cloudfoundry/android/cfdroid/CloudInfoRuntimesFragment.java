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
public class CloudInfoRuntimesFragment extends Fragment implements HasTitle{

	@ViewById(android.R.id.list)
	ListView listView;
	
	@StringRes(R.string.ci_info_runtimes)
	String title;
	
	@Override
	public CharSequence getTitle() {
		return title;
	}
	
	@AfterViews
	void afterViews() {
		listView.setAdapter(new RuntimeListAdapter());
	}
	
	private List<CloudInfo.Runtime> runtimes = new ArrayList<CloudInfo.Runtime>();
	
	void updateData(CloudInfo info) {
		runtimes = new ArrayList<CloudInfo.Runtime>( info.getRuntimes());
		if (listView != null && listView.getAdapter() != null) {
			((BaseAdapter)listView.getAdapter()).notifyDataSetChanged();
		}
	}
	
	
	private final class RuntimeListAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return runtimes.size();
		}

		@Override
		public CloudInfo.Runtime getItem(int position) {
			return runtimes.get(position) ;
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
				nameView = (TextView) convertView.findViewById(android.R.id.text1);
				convertView.setTag(R.id.name, nameView);
			} else {
				nameView = (TextView) convertView.getTag(R.id.name);
			}

			nameView.setText(getItem(position).getDescription());
			return convertView;		}
		
	}
}
