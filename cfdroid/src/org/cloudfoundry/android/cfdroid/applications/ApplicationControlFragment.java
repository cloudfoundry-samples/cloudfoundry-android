package org.cloudfoundry.android.cfdroid.applications;

import org.cloudfoundry.android.cfdroid.Clients;
import org.cloudfoundry.android.cfdroid.R;
import org.cloudfoundry.android.cfdroid.support.HasTitle;
import org.cloudfoundry.android.cfdroid.support.masterdetail.DataHolder;
import org.cloudfoundry.client.lib.CloudApplication;
import org.cloudfoundry.client.lib.CloudApplication.AppState;
import org.cloudfoundry.client.lib.CloudInfo;

import roboguice.inject.InjectResource;
import roboguice.inject.InjectView;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.SeekBar;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.github.rtyley.android.sherlock.roboguice.fragment.RoboSherlockFragment;
import com.google.inject.Inject;

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

	@Inject
	private Clients clients;

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

		CloudApplication cloudApplication = getCloudApplication();
		AppState state = cloudApplication.getState();
		startBtn.setEnabled(state == AppState.STOPPED);
		stopBtn.setEnabled(state == AppState.STARTED);

		CloudInfo info = clients.getCloudInfo();
		int maxTotalMemory = info.getLimits().getMaxTotalMemory();

		instancesSeekBar.setMax(16); // ?
		instancesSeekBar.setProgress(cloudApplication.getInstances());

		memorySeekBar.setMax(maxTotalMemory);
		memorySeekBar.setProgress(cloudApplication.getMemory());
		int totalMemoryBefore = info.getUsage().getTotalMemory();
		int currentlyUsedByThisApp = cloudApplication.getInstances()
				* cloudApplication.getMemory();
		memorySeekBar.setSecondaryProgress(maxTotalMemory
				- totalMemoryBefore + currentlyUsedByThisApp);
		
		overallMemoryPb.setMax(maxTotalMemory);

	}

	private CloudApplication getCloudApplication() {
		return ((DataHolder<CloudApplication>) getActivity()).getSelectedItem();
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.app_control, menu);
		menu.findItem(R.id.cloud_apply).setEnabled(false);
	}

}
