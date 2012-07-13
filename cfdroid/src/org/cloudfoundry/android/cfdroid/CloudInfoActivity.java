package org.cloudfoundry.android.cfdroid;

import org.cloudfoundry.android.cfdroid.CloudInfoFrameworksFragment_;
import org.cloudfoundry.android.cfdroid.CloudInfoRuntimesFragment_;
import org.cloudfoundry.android.cfdroid.menu.AbstractFragmentNotRootActivity_;
import org.cloudfoundry.android.cfdroid.R;
import org.cloudfoundry.client.lib.CloudInfo;

import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.Window;
import org.cloudfoundry.android.cfdroid.CloudInfoBasicFragment_;
import com.googlecode.androidannotations.annotations.AfterInject;
import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.Bean;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.OptionsItem;
import com.googlecode.androidannotations.annotations.OptionsMenu;
import com.googlecode.androidannotations.annotations.UiThread;
import com.googlecode.androidannotations.annotations.ViewById;
import com.viewpagerindicator.TitlePageIndicator;
import com.viewpagerindicator.TitlePageIndicator.IndicatorStyle;

@EActivity(R.layout.cloud_info_tabs)
public class CloudInfoActivity extends AbstractFragmentNotRootActivity_ {

	@Bean
	// Can't use until https://github.com/excilys/androidannotations/issues/263 is resolved
	// @NonConfigurationInstance
	CloudInfoDLTask task;

	private boolean hasData = false;

	CloudInfoBasicFragment basic;
	
	CloudInfoRuntimesFragment runtimes;
	
	CloudInfoFrameworksFragment frameworks;

	@ViewById
	ViewPager pager;

	@ViewById(R.id.indicator)
	TitlePageIndicator pageIndicator;

	PagerAdapter pagerAdapter;

	@AfterInject
	void afterInject() {
		
		// TODO understand why @FragmentById and <fragment> don't work
		basic = new CloudInfoBasicFragment_();
		runtimes = new CloudInfoRuntimesFragment_();
		frameworks = new CloudInfoFrameworksFragment_();
		
		pagerAdapter = new StaticFragmentPagerAdapter(
				getSupportFragmentManager(), basic, runtimes, frameworks);
	}

	@AfterViews
	void afterViews() {
		pager.setAdapter(pagerAdapter);
		pageIndicator.setViewPager(pager);

		final float density = getResources().getDisplayMetrics().density;
		pageIndicator.setBackgroundColor(0x18FF0000);
		pageIndicator.setFooterColor(0xFFAA2222);
		pageIndicator.setFooterLineHeight(1 * density); // 1dp
		pageIndicator.setFooterIndicatorHeight(3 * density); // 3dp
		pageIndicator.setFooterIndicatorStyle(IndicatorStyle.Underline);
		pageIndicator.setTextColor(0xAA000000);
		pageIndicator.setSelectedColor(0xFF000000);
		pageIndicator.setSelectedBold(true);
	}

	@UiThread
	void showDLStatus() {
		setSupportProgressBarIndeterminateVisibility(task.isDlInProgress());
		invalidateOptionsMenu();
	}

	@UiThread
	void updateData(CloudInfo info) {
		hasData = true;
		basic.updateData(info);
		runtimes.updateData(info);
		frameworks.updateData(info);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		workaroundIssueAA263();

		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		showDLStatus();
	}

	private void workaroundIssueAA263() {
		CloudInfoActivity.NonConfigurationInstancesHolder nonConfigurationInstance = ((CloudInfoActivity.NonConfigurationInstancesHolder) super
				.getLastCustomNonConfigurationInstance());
		if (nonConfigurationInstance != null) {
			task = nonConfigurationInstance.task;
			((org.cloudfoundry.android.cfdroid.CloudInfoDLTask_) task)
					.rebind(this);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		// Hide the refresh button if DL is in progress
		// This will give the impression that the progress
		// indicator replaces it.
		menu.findItem(R.id.refresh).setVisible(!task.isDlInProgress());
		return true;
	}

	@AfterViews
	void startDLOrShow() {
		if (!hasData) {
			task.dl();
		}
	}

	@OptionsItem(R.id.refresh)
	void refresh() {
		task.dl();
	}

	// Workaround AA issue 263

	@Override
	public CloudInfoActivity.NonConfigurationInstancesHolder onRetainCustomNonConfigurationInstance() {
		return new CloudInfoActivity.NonConfigurationInstancesHolder(
				super.onRetainCustomNonConfigurationInstance(), task);
	}

	@Override
	public Object getLastCustomNonConfigurationInstance() {
		CloudInfoActivity.NonConfigurationInstancesHolder nonConfigurationInstance = ((CloudInfoActivity.NonConfigurationInstancesHolder) super
				.getLastCustomNonConfigurationInstance());
		if (nonConfigurationInstance == null) {
			return null;
		}
		return nonConfigurationInstance.superNonConfigurationInstance;
	}

	private static class NonConfigurationInstancesHolder {

		public final Object superNonConfigurationInstance;
		public final CloudInfoDLTask task;

		public NonConfigurationInstancesHolder(
				Object superNonConfigurationInstance, CloudInfoDLTask task) {
			this.superNonConfigurationInstance = superNonConfigurationInstance;
			this.task = task;
		}

	}

}
