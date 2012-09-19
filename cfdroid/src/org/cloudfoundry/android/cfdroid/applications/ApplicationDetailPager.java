package org.cloudfoundry.android.cfdroid.applications;

import org.cloudfoundry.android.cfdroid.R;
import org.cloudfoundry.android.cfdroid.support.masterdetail.DetailPaneEventsCallback;
import org.cloudfoundry.android.cfdroid.support.masterdetail.DetailPaneWithViewPager;

import android.support.v4.app.Fragment;
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
	protected DetailPaneWithViewPager.DetailPanePagerAdapter buildPagerAdapter() {
		return new ApplicationPagerAdapter();
	}

	private class ApplicationPagerAdapter extends
			DetailPaneWithViewPager.DetailPanePagerAdapter {

		public ApplicationPagerAdapter() {
			super(getActivity().getSupportFragmentManager());
		}

		private Fragment[] fragments = new Fragment[] { //
		new ApplicationControlFragment(), //
				new ApplicationServicesFragment(), //
				new ApplicationInstanceStatsFragment() //
		};
		private int[] titles = new int[] { //
		R.string.control, //
				R.string.services, //
				R.string.instances //
		};

		@Override
		public Fragment getItem(int index) {
			Fragment f = fragments[index];
			((DetailPaneEventsCallback) f).selectionChanged(position);
			return f;
		}

		@Override
		public int getCount() {
			return fragments.length;
		}

		@Override
		public CharSequence getPageTitle(int index) {
			return getResources().getText(titles[index]);
		}

		@Override
		public void selectionChanged(int position) {
			for (int i = 0 ; i < fragments.length ; i++) {
				((DetailPaneEventsCallback) fragments[i]).selectionChanged(position);
			}
		}

	}

}
