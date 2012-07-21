package org.cloudfoundry.android.cfdroid.applications;

import org.cloudfoundry.android.cfdroid.R;
import org.cloudfoundry.android.cfdroid.support.HasTitle;
import org.cloudfoundry.android.cfdroid.support.masterdetail.DataHolder;
import org.cloudfoundry.client.lib.CloudApplication;

import roboguice.inject.InjectResource;
import roboguice.inject.InjectView;
import roboguice.util.Ln;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.SeekBar;

import com.github.rtyley.android.sherlock.roboguice.fragment.RoboSherlockFragment;

public class ApplicationControlFragment extends RoboSherlockFragment implements
		HasTitle {
	
	@InjectView(R.id.start)
	private View startBtn;
	
	@InjectView(R.id.stop)
	private View stopBtn;
	
	@InjectView(R.id.restart)
	private View restartBtn;
	
	@InjectView(R.id.instances_seekbar)
	private SeekBar instancesSeekBar;
	
	@InjectView(R.id.memory_seekbar)
	private SeekBar memorySeekBar;

	@InjectView(R.id.overall_memory_progressbar)
	private ProgressBar overallMemoryPb;
	
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
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		
		
	}
	
	private CloudApplication getApplication() {
		return ((DataHolder<CloudApplication>)getActivity()).getSelectedItem();
	}


}
