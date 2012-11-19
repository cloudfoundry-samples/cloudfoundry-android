/*
 * Copyright 2012 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.cloudfoundry.android.cfdroid.cloud;

import org.cloudfoundry.android.cfdroid.R;

import roboguice.inject.ContentView;
import roboguice.inject.InjectView;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;

import com.github.rtyley.android.sherlock.roboguice.activity.RoboSherlockFragmentActivity;
import com.viewpagerindicator.TitlePageIndicator;

/**
 * An activity to display various info about the target cloud, like supported
 * languages and runtimes, <i>etc.</i>
 * 
 * @author Eric Bottard
 * 
 */
@ContentView(R.layout.pager_with_title)
public class CloudInfoActivity extends RoboSherlockFragmentActivity {

	@InjectView(R.id.title_page_indicator)
	private TitlePageIndicator pageIndicator;
	@InjectView(R.id.pager)
	private ViewPager pager;
	private PagerAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		adapter = buildPagerAdapter();
		pager.setAdapter(adapter);
		pageIndicator.setViewPager(pager);

	}

	private PagerAdapter buildPagerAdapter() {
		return new FragmentPagerAdapter(getSupportFragmentManager()) {

			@Override
			public int getCount() {
				return 4;
			}

			@Override
			public Fragment getItem(int position) {
				switch (position) {
				case 0:
					return new RuntimesFragment();
				case 1:
					return new FrameworksFragment();
				case 2:
					return new ServiceConfigurationsFragment();
				case 3:
					return new BasicInfoFragment();
				default:
					throw new IllegalStateException();
				}
			}

			@Override
			public CharSequence getPageTitle(int position) {
				switch (position) {
				case 0:
					return getResources().getText(R.string.runtimes);
				case 1:
					return getResources().getText(R.string.frameworks);
				case 2:
					return getResources().getText(
							R.string.service_configurations);
				case 3:
					return getResources().getText(R.string.ci_basic_title);
				default:
					throw new IllegalStateException();
				}
			}
		};
	}
}
