package org.cloudfoundry.android.cfdroid.targets;


import org.cloudfoundry.android.cfdroid.R;
import org.cloudfoundry.android.cfdroid.R.id;
import org.cloudfoundry.android.cfdroid.R.layout;

import android.content.Context;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.googlecode.androidannotations.annotations.EViewGroup;
import com.googlecode.androidannotations.annotations.ViewById;

@EViewGroup(R.layout.target_select_item)
public class TargetSelectionView extends LinearLayout{

	public TargetSelectionView(Context context) {
		super(context);
	}
	
	@ViewById(R.id.checkbox)
	CheckBox checkBox;
	
	@ViewById(R.id.label)
	TextView label;
	
	@ViewById(R.id.url)
	TextView url;
	
	public void bind(CloudTarget target, boolean checked) {
		label.setText(target.getLabel());
		url.setText(target.getURL());
		checkBox.setChecked(checked);
	}

	public CheckBox getCheckBox() {
		return checkBox;
	}
	
	
	
}