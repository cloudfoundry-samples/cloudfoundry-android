package org.cloudfoundry.android.cfdroid.applications;

import javax.annotation.Nullable;

import org.cloudfoundry.android.cfdroid.R;

import roboguice.inject.ContentView;
import roboguice.inject.InjectFragment;
import roboguice.inject.InjectView;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.widget.FrameLayout;

import com.github.rtyley.android.sherlock.roboguice.activity.RoboSherlockFragmentActivity;

@ContentView(R.layout.applications)
public class ApplicationsActivity extends RoboSherlockFragmentActivity
		implements ApplicationsListFragment.OnApplicationSelectedListener {

	@Nullable
	@InjectFragment(R.id.applications_list)
	ApplicationsListFragment listFragment;

	@Nullable
	@InjectFragment(R.id.application_tabs)
	ApplicationDetailViewPager viewPagerFragment;

	@Nullable
	@InjectView(R.id.fragment_container)
	FrameLayout fragmentContainer;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// If not null, we're in the large layout
		if (fragmentContainer != null) {
			if (savedInstanceState != null) {
				return;
			}

			ApplicationsListFragment initialFragment = new ApplicationsListFragment();

			getSupportFragmentManager().beginTransaction()
					.add(R.id.fragment_container, initialFragment).commit();
		}
	}

	@Override
	public void onApplicationSelected(int position) {
		if (viewPagerFragment != null) {
			// two-pane layout, direct update
			viewPagerFragment.displayApplication(position);
		} else {
			ApplicationDetailViewPager rightFragment = new ApplicationDetailViewPager();
			Bundle args = new Bundle();
			args.putInt(ApplicationDetailViewPager.ARG_APPLICATION_INDEX, position);
			rightFragment.setArguments(args);
			FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
			transaction.replace(R.id.fragment_container, rightFragment);
			transaction.addToBackStack(null);
			transaction.commit();
		}
	}
}
