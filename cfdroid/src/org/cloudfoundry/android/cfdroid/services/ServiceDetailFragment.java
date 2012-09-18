package org.cloudfoundry.android.cfdroid.services;

import org.cloudfoundry.android.cfdroid.support.masterdetail.DetailPaneEventsCallback;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.rtyley.android.sherlock.roboguice.fragment.RoboSherlockFragment;

public class ServiceDetailFragment extends RoboSherlockFragment implements DetailPaneEventsCallback{

	@Override
	public void selectionChanged(int position) {
		// TODO Auto-generated method stub
		
	}
	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		TextView textView = new TextView(getActivity());
		textView.setText("Hello");
		return textView;
	}
}
