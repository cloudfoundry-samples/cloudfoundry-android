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
package org.cloudfoundry.android.cfdroid.services;

import org.cloudfoundry.android.cfdroid.R;
import org.cloudfoundry.android.cfdroid.support.BaseViewHolder;
import org.cloudfoundry.client.lib.CloudService;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * A view for displaying info about an available service engine.
 * 
 * @author Eric Bottard
 * 
 */
public class ServiceView extends BaseViewHolder<CloudService> {

	protected TextView name;

	protected ImageView logo;

	public ServiceView(View container) {
		super();
		name = (TextView) container.findViewById(R.id.name);
		logo = (ImageView) container.findViewById(R.id.logo);
	}

	@Override
	public void bind(CloudService service) {
		name.setText(service.getName());
		ServiceLogos logoEnum;
		try {
			logoEnum = ServiceLogos.valueOf(service.getVendor());
		} catch (IllegalArgumentException notFound) {
			logoEnum = ServiceLogos.unknown;
		}
		logo.setImageLevel(logoEnum.level);
	}

}
