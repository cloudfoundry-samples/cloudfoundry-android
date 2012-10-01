package org.cloudfoundry.android.cfdroid.support.masterdetail;

import org.cloudfoundry.android.cfdroid.R;

import roboguice.inject.InjectView;
import roboguice.util.Ln;
import android.os.Bundle;
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
 * @author Eric Bottard
 * 
 */
public abstract class DetailPaneWithViewPager extends RoboSherlockFragment
		implements DetailPaneEventsCallback {

	private static final int POSITION_NONE = -100;
	@InjectView(R.id.title_page_indicator)
	private TitlePageIndicator pageIndicator;
	@InjectView(R.id.pager)
	private ViewPager pager;
	private DetailPanePagerAdapter adapter;

	protected int position = POSITION_NONE;

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
		if (position != POSITION_NONE) {
			adapter.selectionChanged(position);
		}
	}

	protected abstract DetailPanePagerAdapter buildPagerAdapter();

	@Override
	public void selectionChanged(int position) {
		this.position = position;
		if (adapter != null) {
			adapter.selectionChanged(position);
			getView().findViewById(R.id.empty).setVisibility(View.GONE);
			getView().findViewById(R.id.content).setVisibility(View.VISIBLE);
		}

	}

}