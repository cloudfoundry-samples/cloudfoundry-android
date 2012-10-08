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
import org.cloudfoundry.android.cfdroid.support.BaseViewHolder;
import org.cloudfoundry.client.lib.CloudInfo.Framework;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class FrameworkView extends BaseViewHolder<Framework> {
	
	private ImageView logo;
	
	private TextView name;

	public FrameworkView(View container) {
		logo = (ImageView) container.findViewById(R.id.logo);
		name = (TextView) container.findViewById(R.id.name);
	}

	@Override
	public void bind(Framework item) {
		FrameworkLogos fl = FrameworkLogos.unknown;
		try {
			 fl = FrameworkLogos.valueOf(item.getName());
		} catch (IllegalArgumentException ignore) {
		}
		logo.setImageLevel(fl.level);
		name.setText(item.getName());
	}

}
