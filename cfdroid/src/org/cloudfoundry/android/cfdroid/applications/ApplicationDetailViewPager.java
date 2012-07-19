package org.cloudfoundry.android.cfdroid.applications;

import org.cloudfoundry.android.cfdroid.R;
import org.cloudfoundry.android.cfdroid.applications.ApplicationsListFragment.Listener;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.rtyley.android.sherlock.roboguice.fragment.RoboSherlockFragment;

public class ApplicationDetailViewPager extends RoboSherlockFragment {

	public static final String ARG_APPLICATION = "application";
	
	private int position = -1;

	private TextView child;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.application_tabs, container, false);
		
		child = new TextView(getActivity());
		((ViewGroup)view).addView(child);
		if (savedInstanceState != null) {
			position = savedInstanceState.getInt(ARG_APPLICATION);
		}
		
		return view;
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt(ARG_APPLICATION, position);
	}
	
	@Override
	public void onStart() {
		super.onStart();
		Bundle args = getArguments();
		if (args != null) {
			updateApplication(args.getInt(ARG_APPLICATION));
		} else if (position != -1) {
			updateApplication(position);
		}
	}
	
	private Listener getListener() {
		return (Listener) getActivity();
	}
	
	public void updateApplication(int position) {
		this.position = position;
		child.setText(getListener().getApplication(position).getName());
	}
}
