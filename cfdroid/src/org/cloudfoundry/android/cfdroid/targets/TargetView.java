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
package org.cloudfoundry.android.cfdroid.targets;

import org.cloudfoundry.android.cfdroid.R;
import org.cloudfoundry.android.cfdroid.support.BaseViewHolder;

import android.view.View;
import android.widget.TextView;

public class TargetView extends BaseViewHolder<CloudTarget>{

	protected TextView label;
	protected TextView url;
	
	public TargetView(View container) {
		label = (TextView)container.findViewById(R.id.label);
		url = (TextView)container.findViewById(R.id.url);
	}
	
	public void bind(CloudTarget target) {
		label.setText(target.getLabel());
		url.setText(target.getURL());
	}

}
