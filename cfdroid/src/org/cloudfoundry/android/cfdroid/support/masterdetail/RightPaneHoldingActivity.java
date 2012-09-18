package org.cloudfoundry.android.cfdroid.support.masterdetail;

import org.cloudfoundry.android.cfdroid.R;

import roboguice.inject.InjectFragment;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.github.rtyley.android.sherlock.roboguice.activity.RoboSherlockFragmentActivity;

public class RightPaneHoldingActivity extends RoboSherlockFragmentActivity {

	@InjectFragment(R.id.right_pane)
	private Fragment rightPane;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(getIntent().getExtras().getInt(
				MasterDetailEventsCallback.RIGHT_PANE_LAYOUT_ID));


	}
	
	@Override
	protected void onStart() {
		super.onStart();
		int position = getIntent().getExtras().getInt(
				MasterDetailEventsCallback.KEY_SELECTION, -1);
		if (position == -1) {
			throw new IllegalStateException();
		}
		((DetailPaneEventsCallback)rightPane).selectionChanged(position);
		
	}

}
