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
import org.cloudfoundry.client.lib.CloudInfo.Runtime;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class RuntimeView extends BaseViewHolder<Runtime> {
	
	private TextView name;
	
	private ImageView logo;
	
	private TextView version;

	public RuntimeView(View view) {
		name = (TextView) view.findViewById(R.id.name);
		logo = (ImageView) view.findViewById(R.id.logo);
		version = (TextView) view.findViewById(R.id.version);
	}

	@Override
	public void bind(Runtime runtime) {
		String runtimeName = runtime.getName();
		RuntimeLogos bestMatch = RuntimeLogos.bestMatch(runtimeName);
		logo.setImageLevel(bestMatch.level);
		version.setText(runtime.getVersion());
		name.setText(runtime.getDescription());
	}

}
