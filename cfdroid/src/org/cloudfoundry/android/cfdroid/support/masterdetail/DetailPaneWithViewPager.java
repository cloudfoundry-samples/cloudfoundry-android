package org.cloudfoundry.android.cfdroid.support.masterdetail;

import org.cloudfoundry.android.cfdroid.R;

import roboguice.inject.InjectView;
import roboguice.util.RoboAsyncTask;

import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.rtyley.android.sherlock.roboguice.fragment.RoboSherlockFragment;
import com.viewpagerindicator.TitlePageIndicator;

public abstract class DetailPaneWithViewPager extends RoboSherlockFragment
		implements DetailPaneEventsCallback {

	@InjectView(R.id.title_page_indicator)
	private TitlePageIndicator pageIndicator;
	@InjectView(R.id.pager)
	private ViewPager pager;
	private PagerAdapter adapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.pager_with_title, container, false);
	}

	@Override
	public void onStart() {
		super.onStart();
		// see
		// http://stackoverflow.com/questions/7700226/display-fragment-viewpager-within-a-fragment
		
		// Also try http://stackoverflow.com/questions/6221763/android-can-you-nest-fragments/6222287#6222287
		if (pager.getAdapter() == null) {
			new RoboAsyncTask<Void>(getActivity()) {

				@Override
				public Void call() throws Exception {
					return null;
				}

				protected void onFinally() throws RuntimeException {
					adapter = buildPagerAdapter();
					pager.setAdapter(adapter);
					pageIndicator.setViewPager(pager);
				}

			}.execute();
		}
	}
	
	protected abstract PagerAdapter buildPagerAdapter();


	@Override
	public void selectionChanged() {
		adapter.notifyDataSetChanged();
	}

}