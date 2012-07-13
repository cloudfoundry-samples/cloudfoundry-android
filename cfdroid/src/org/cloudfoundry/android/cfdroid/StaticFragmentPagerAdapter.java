package org.cloudfoundry.android.cfdroid;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class StaticFragmentPagerAdapter extends FragmentPagerAdapter {

	private Fragment[] fragments;

	public StaticFragmentPagerAdapter(FragmentManager fragmentManager,
			Fragment... fragments) {
		super(fragmentManager);
		this.fragments = fragments;
	}

	@Override
	public Fragment getItem(int i) {
		return fragments[i];
	}

	@Override
	public int getCount() {
		return fragments.length;
	}

	@Override
	public CharSequence getPageTitle(int position) {
		return ((HasTitle) getItem(position)).getTitle();
	}
}
