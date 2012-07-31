package org.cloudfoundry.android.cfdroid.applications;

import org.cloudfoundry.android.cfdroid.R;
import org.cloudfoundry.android.cfdroid.support.masterdetail.DetailPaneEventsCallback;

import roboguice.inject.InjectView;
import roboguice.util.Ln;
import roboguice.util.RoboAsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.rtyley.android.sherlock.roboguice.fragment.RoboSherlockFragment;
import com.viewpagerindicator.TitlePageIndicator;

public class ApplicationDetailPager extends RoboSherlockFragment implements
		DetailPaneEventsCallback {

	@InjectView(R.id.title_page_indicator)
	private TitlePageIndicator pageIndicator;

	@InjectView(R.id.pager)
	private ViewPager pager;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Ln.i("Pager onCreateView %s", this);
		return inflater.inflate(R.layout.pager_with_title, container, false);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		Ln.i("Pager onCreate %s", this);
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onDestroy() {
		Ln.i("Pager onDestroy %s", this);
		super.onDestroy();
	}

	@Override
	public void onStart() {
		Ln.i("Pager onStart %s", this);
		super.onStart();
		// see
		// http://stackoverflow.com/questions/7700226/display-fragment-viewpager-within-a-fragment
		if (pager.getAdapter() == null) {
			new RoboAsyncTask<Void>(getActivity()) {
				@Override
				public Void call() throws Exception {
					return null;
				}

				protected void onFinally() throws RuntimeException {
					pager.setAdapter(new ApplicationPagerAdapter());
					pageIndicator.setViewPager(pager);
				}

			}.execute();
		}
	}

	@Override
	public void selectionChanged() {
		// TODO Auto-generated method stub

	}

	private class ApplicationPagerAdapter extends FragmentPagerAdapter {

		public ApplicationPagerAdapter() {
			super(getActivity().getSupportFragmentManager());
		}

		@Override
		public Fragment getItem(int position) {
			switch (position) {
			case 0:
				return new ApplicationControlFragment();
			case 1:
				return new ApplicationServicesFragment();
			case 2:
				return new ApplicationInstanceStatsFragment();
			default:
				throw new IllegalStateException();
			}
		}

		@Override
		public int getCount() {
			return 3;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			switch (position) {
			case 0:
				return getResources().getText(R.string.control);
			case 1:
				return getResources().getText(R.string.services);
			case 2:
				return getResources().getText(R.string.instances);
			default:
				throw new IllegalStateException();
			}
		}

	}

}
