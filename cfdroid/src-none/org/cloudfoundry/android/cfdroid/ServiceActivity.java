package org.cloudfoundry.android.cfdroid;

import org.cloudfoundry.android.cfdroid.menu.AbstractFragmentNotRootActivity_;
import org.cloudfoundry.android.cfdroid.R;
import org.cloudfoundry.client.lib.CloudService;

import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;

import org.cloudfoundry.android.cfdroid.ApplicationServicesFragment_;
import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.Bean;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.Extra;
import com.googlecode.androidannotations.annotations.OptionsMenu;
import com.googlecode.androidannotations.annotations.ViewById;
import com.viewpagerindicator.TitlePageIndicator;
import com.viewpagerindicator.TitlePageIndicator.IndicatorStyle;

@EActivity(R.layout.service_tabs)
public class ServiceActivity extends AbstractFragmentNotRootActivity_ {

	@ViewById
	ViewPager pager;
	
	@ViewById(R.id.indicator)
	TitlePageIndicator pageIndicator;

	PagerAdapter pagerAdapter;

	@Extra
	String serviceName;
 
	@Bean
	Clients clients;

	CloudService getCloudService() {
		return clients.getService(serviceName);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		pagerAdapter = new StaticFragmentPagerAdapter(
				getSupportFragmentManager(),
				new ApplicationServicesFragment_(),
				new ApplicationServicesFragment_(),
				new ApplicationServicesFragment_(),
				new ApplicationServicesFragment_(),
				new ApplicationServicesFragment_(),
				new ApplicationServicesFragment_());
	}

	@AfterViews
	void afterViews() {
		pager.setAdapter(pagerAdapter);
		pageIndicator.setViewPager(pager);

		final float density = getResources().getDisplayMetrics().density;
        pageIndicator.setBackgroundColor(0x18FF0000);
        pageIndicator.setFooterColor(0xFFAA2222);
        pageIndicator.setFooterLineHeight(1 * density); //1dp
        pageIndicator.setFooterIndicatorHeight(3 * density); //3dp
        pageIndicator.setFooterIndicatorStyle(IndicatorStyle.Underline);
        pageIndicator.setTextColor(0xAA000000);
        pageIndicator.setSelectedColor(0xFF000000);
        pageIndicator.setSelectedBold(true);}
}
