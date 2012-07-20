package org.cloudfoundry.android.cfdroid.applications;

import org.cloudfoundry.android.cfdroid.R;
import org.cloudfoundry.android.cfdroid.support.HasTitle;
import org.cloudfoundry.client.lib.CloudApplication;

import roboguice.inject.InjectResource;
import roboguice.util.Ln;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.rtyley.android.sherlock.roboguice.fragment.RoboSherlockFragment;

public class ApplicationControlFragment extends RoboSherlockFragment implements
		HasTitle {
	
	@InjectResource(R.string.control)
	private String title;

	@Override
	public CharSequence getTitle() {
		return title;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.application_control, container, false);
	}


}
