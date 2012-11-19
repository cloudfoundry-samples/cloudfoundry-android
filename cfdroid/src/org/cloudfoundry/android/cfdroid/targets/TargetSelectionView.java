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

import android.view.View;
import android.widget.CheckBox;

public class TargetSelectionView extends TargetView {

	public TargetSelectionView(View container) {
		super(container);
		checkBox = (CheckBox) container.findViewById(R.id.checkbox);
	}

	CheckBox checkBox;

	public void bind(CloudTarget target, boolean checked) {
		super.bind(target);
		checkBox.setChecked(checked);
	}

	public CheckBox getCheckBox() {
		return checkBox;
	}

}