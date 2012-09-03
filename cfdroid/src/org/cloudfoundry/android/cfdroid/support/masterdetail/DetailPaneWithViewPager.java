package org.cloudfoundry.android.cfdroid.support.masterdetail;

import org.cloudfoundry.android.cfdroid.R;

import roboguice.inject.InjectView;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.rtyley.android.sherlock.roboguice.fragment.RoboSherlockFragment;
import com.viewpagerindicator.TitlePageIndicator;

/**
 * Base class for handling having a ViewPager using {@link Fragment}s inside
 * another Fragment. This is definitely not something you want to let your kids
 * try at home, as witnessed by numerous posts on StackOverflow.
 * 
 * The solution used here is adapted from 
 * http://stackoverflow.com/questions/6221763/android-can-you-nest-fragments/6222287#9700314
 * 
 * @author Eric Bottard
 * 
 */
public abstract class DetailPaneWithViewPager extends RoboSherlockFragment
		implements DetailPaneEventsCallback {

	@InjectView(R.id.title_page_indicator)
	private TitlePageIndicator pageIndicator;
	@InjectView(R.id.pager)
	private ViewPager pager;
	private PagerAdapter adapter;

	private final Handler handler = new Handler(Looper.getMainLooper());
	private Runnable runPager;
	private boolean created = false;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.pager_or_empty, container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		if (runPager != null)
			handler.post(runPager);
		created = true;
	}

	@Override
	public void onPause() {
		super.onPause();
		handler.removeCallbacks(runPager);
	}

	protected void setAdapter(PagerAdapter a) {
		this.adapter = a;
		runPager = new Runnable() {
			@Override
			public void run() {
				pager.setAdapter(adapter);
				pageIndicator.setViewPager(pager);
			}
		};
		if (created) {
			handler.post(runPager);
		}
	}

	@Override
	public void onStart() {
		super.onStart();
		setAdapter(buildPagerAdapter());
	}

	protected abstract PagerAdapter buildPagerAdapter();

	@Override
	public void selectionChanged() {
		adapter.notifyDataSetChanged();
		getView().findViewById(R.id.empty).setVisibility(View.GONE);
		getView().findViewById(R.id.content).setVisibility(View.VISIBLE);
		
	}

}