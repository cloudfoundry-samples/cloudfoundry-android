package org.cloudfoundry.android.cfdroid.applications;

import org.cloudfoundry.android.cfdroid.R;

import roboguice.inject.InjectExtra;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.rtyley.android.sherlock.roboguice.fragment.RoboSherlockFragment;

public class ApplicationDetailViewPager extends RoboSherlockFragment {

	public static final String ARG_APPLICATION_INDEX = "index";
	
	private int applicationIndex;

	private TextView child;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.application_tabs, container, false);
		
		child = new TextView(getActivity());
		((ViewGroup)view).addView(child);
		
		
		return view;
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		Bundle arguments = (getArguments() != null) ? getArguments() : new Bundle();
		int idx = arguments.getInt(ARG_APPLICATION_INDEX, -1);
		
		if(idx>0) {
			applicationIndex = idx;
		}
		displayApplication(applicationIndex);
	}
	
	public void displayApplication(int index) {
		child.setText("idx " + index);
	}
}
