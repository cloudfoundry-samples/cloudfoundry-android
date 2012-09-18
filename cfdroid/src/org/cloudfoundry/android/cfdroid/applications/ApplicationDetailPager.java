package org.cloudfoundry.android.cfdroid.applications;

import org.cloudfoundry.android.cfdroid.R;
import org.cloudfoundry.android.cfdroid.support.masterdetail.DetailPaneEventsCallback;
import org.cloudfoundry.android.cfdroid.support.masterdetail.DetailPaneWithViewPager;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;

/**
 * The main Fragment for managing an app. Embeds other fragments in a
 * {@link ViewPager}.
 * 
 * @author Eric Bottard
 * 
 */
public class ApplicationDetailPager extends DetailPaneWithViewPager {

	@Override
	protected PagerAdapter buildPagerAdapter() {
		return new ApplicationPagerAdapter();
	}

	private class ApplicationPagerAdapter extends FragmentPagerAdapter {

		public ApplicationPagerAdapter() {
			super(getActivity().getSupportFragmentManager());
		}

		@Override
		public Fragment getItem(int index) {
			Fragment f = null;
			switch (index) {
			case 0:
				f = new ApplicationControlFragment();
				break;
			case 1:
				f = new ApplicationServicesFragment();
				break;
			case 2:
				f = new ApplicationInstanceStatsFragment();
				break;
			default:
				throw new IllegalStateException();
			}
			((DetailPaneEventsCallback)f).selectionChanged(position);
			return f;
		}

		@Override
		public int getCount() {
			return 3;
		}

		@Override
		public CharSequence getPageTitle(int index) {
			switch (index) {
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

//		@Override
//		public int getItemPosition(Object object) {
//			return PagerAdapter.POSITION_NONE;
//		}

	}

}
