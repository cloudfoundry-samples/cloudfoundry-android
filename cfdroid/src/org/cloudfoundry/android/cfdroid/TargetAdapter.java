package org.cloudfoundry.android.cfdroid;

import java.util.ArrayList;
import java.util.List;

import org.cloudfoundry.android.cfdroid.R;
import org.cloudfoundry.android.cfdroid.model.CloudTarget;


import com.googlecode.androidannotations.annotations.EBean;
import com.googlecode.androidannotations.annotations.RootContext;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

@EBean
public class TargetAdapter extends BaseAdapter {
	
	protected List<CloudTarget> targets = new ArrayList<CloudTarget>();
	
	protected int layout = R.layout.target_item;
	
	public void setLayout(int layout) {
		this.layout = layout;
	}

	@RootContext
	Context context;
	
	public void setTargets(List<CloudTarget> targets) {
		this.targets = targets;
	}

	@Override
	public int getCount() {
		return targets.size();
	}

	@Override
	public CloudTarget getItem(int position) {
		return targets.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		TextView labelView;
		TextView urlView;
		if (convertView == null) {
			convertView = View.inflate(context,
					layout, null);
			labelView = (TextView) convertView.findViewById(R.id.label);
			urlView = (TextView) convertView
					.findViewById(R.id.url);
			convertView.setTag(R.id.label, labelView);
			convertView.setTag(R.id.url, urlView);
		} else {
			labelView = (TextView) convertView.getTag(R.id.label);
			urlView = (TextView) convertView.getTag(R.id.url);
		}

		labelView.setText(getItem(position).getLabel());
		urlView.setText(getItem(position).getURL());
		return convertView;	}

}
