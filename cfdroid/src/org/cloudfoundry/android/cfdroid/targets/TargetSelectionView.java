package org.cloudfoundry.android.cfdroid.targets;


import org.cloudfoundry.android.cfdroid.R;

import android.view.View;
import android.widget.CheckBox;

public class TargetSelectionView extends TargetView{

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