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
package org.cloudfoundry.android.cfdroid.applications;

import org.cloudfoundry.android.cfdroid.R;
import org.cloudfoundry.android.cfdroid.support.masterdetail.MasterDetailActivity;

import roboguice.inject.ContentView;

/**
 * Master/Detail activity for applications. Shows a list of applications and
 * details about one on the next/right screen.
 * 
 * @author Eric Bottard
 * 
 */
@ContentView(R.layout.applications)
public class ApplicationsActivity extends
		MasterDetailActivity<ApplicationDetailPager> {

	@Override
	protected int rightPaneLayout() {
		return R.layout.right_pane_applications;
	}

}
