package org.cloudfoundry.android.cfdroid.support.masterdetail;

import java.util.Map;

import org.cloudfoundry.android.cfdroid.R;

import roboguice.inject.InjectView;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
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
 * http://stackoverflow.com/questions/6221763
 * /android-can-you-nest-fragments/6222287#9700314
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
	private DetailPanePagerAdapter adapter;

	protected int position;

	/**
	 * An extension of {@link PagerAdapter} that allows broadcasting the left
	 * pane selection change event to members of the {@link ViewPager}.
	 * 
	 * @author Eric Bottard
	 * 
	 */
	public static abstract class DetailPanePagerAdapter extends FragmentPagerAdapter implements DetailPaneEventsCallback {

		public DetailPanePagerAdapter(FragmentManager supportFragmentManager) {
			super(supportFragmentManager);
		}

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.pager_or_empty, container, false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		this.adapter = buildPagerAdapter();
		pager.setAdapter(adapter);
		pageIndicator.setViewPager(pager);
	}

	protected abstract DetailPanePagerAdapter buildPagerAdapter();

	@Override
	public void selectionChanged(int position) {
		this.position = position;
		adapter.selectionChanged(position);
		getView().findViewById(R.id.empty).setVisibility(View.GONE);
		getView().findViewById(R.id.content).setVisibility(View.VISIBLE);

	}

}