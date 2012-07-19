package org.cloudfoundry.android.cfdroid.targets;

import org.cloudfoundry.android.cfdroid.R;

import roboguice.fragment.RoboDialogFragment;
import roboguice.inject.InjectView;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager.LayoutParams;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

public abstract class TargetEditDialogFragment extends RoboDialogFragment
		implements OnEditorActionListener {

	@InjectView(R.id.label)
	private EditText label;

	@InjectView(R.id.url)
	private EditText url;
	
	private CloudTarget result;
	
	public TargetEditDialogFragment(CloudTarget target) {
		this.result = target;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.target_edit, container);
		getDialog().setTitle(R.string.edit_target_dialog_title);

		return view;
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		label.requestFocus();
		getDialog().getWindow().setSoftInputMode(
				LayoutParams.SOFT_INPUT_STATE_VISIBLE);
		
		url.setOnEditorActionListener(this);
		
	}

	public abstract void onResult(CloudTarget result);

	@Override
	public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
		if (EditorInfo.IME_ACTION_DONE == actionId) {
			result.label = label.getText().toString().trim();
			result.URL = url.getText().toString().trim();
			onResult(result);
			this.dismiss();
			return true;
		}
		return false;
	}
}
