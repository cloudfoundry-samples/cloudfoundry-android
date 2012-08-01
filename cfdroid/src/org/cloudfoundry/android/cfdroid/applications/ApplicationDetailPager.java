package org.cloudfoundry.android.cfdroid.applications;

import org.cloudfoundry.android.cfdroid.R;
import org.cloudfoundry.android.cfdroid.support.masterdetail.DetailPaneWithViewPager;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;


public  class ApplicationDetailPager extends DetailPaneWithViewPager {
	
	@Override
	protected PagerAdapter buildPagerAdapter() {
		return new ApplicationPagerAdapter();
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
		
		@Override
		public int getItemPosition(Object object) {
			return PagerAdapter.POSITION_NONE;
		}

	}

}
