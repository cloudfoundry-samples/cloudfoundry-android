package org.cloudfoundry.android.cfdroid.applications;

import org.cloudfoundry.android.cfdroid.R;
import org.cloudfoundry.android.cfdroid.support.StaticFragmentPagerAdapter;
import org.cloudfoundry.android.cfdroid.support.masterdetail.AbstractDetailPane;
import org.cloudfoundry.client.lib.CloudApplication;

import roboguice.inject.InjectView;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.viewpagerindicator.TitlePageIndicator;

public class ApplicationDetailViewPager extends AbstractDetailPane<CloudApplication> {

	@Deprecated
	private TextView child;

	@InjectView(R.id.pager)
	private ViewPager viewPager;

	@InjectView(R.id.indicator)
	private TitlePageIndicator indicator;
	
	@Override
	protected View onCreateViewInternal(LayoutInflater inflater,
			ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.pager_with_title_indicator,
				container, false);
		
		child = new TextView(getActivity());
		((ViewGroup) view).addView(child);
		return view;
	}

	@Override
	public void onStart() {
		super.onStart();
		// see
		// http://stackoverflow.com/questions/7700226/display-fragment-viewpager-within-a-fragment
		new AsyncTask<Void, Void, Void>() {
			@Override
			protected Void doInBackground(Void... params) {
				return null;
			}

			protected void onPostExecute(Void result) {
				StaticFragmentPagerAdapter adapter = new StaticFragmentPagerAdapter(
						getFragmentManager(), new ApplicationControlFragment(),
						new ApplicationControlFragment(),
						new ApplicationControlFragment(),
						new ApplicationControlFragment());
				viewPager.setAdapter(adapter);
				indicator.setViewPager(viewPager);
			};
		}.execute();
		selectionChanged();

	}

	@Override
	public void selectionChanged() {
		
		FragmentPagerAdapter adapter = (FragmentPagerAdapter) viewPager
				.getAdapter();
		if (adapter != null) {
			Fragment f = adapter.getItem(viewPager.getCurrentItem());
		}
		
		CloudApplication selectedItem = getSelectedItem();
		if (selectedItem != null) {
			child.setText(selectedItem.getName());
		}
		
	}
}
